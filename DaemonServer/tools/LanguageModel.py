'''
/**
 * Copyright (c) 2012, University of Delaware
 * All rights reserved.
 *
 * @author: Sergio Pino
 * @author: Keith Elliott
 * Website: http://www.eecis.udel.edu/~pinogal, http://www.eecis.udel.edu/~kelliott
 * emails  : sergiop@udel.edu - kelliott@udel.edu
 * Date   : May, 2012
 *
 */
'''
from WordCount import WordCount
from InformationTheory import InformationTheory
from operator import itemgetter

import math

EPSILON = 0.00000000001


class LanguageModel(object):
    '''
    Implementation of Multinomial Naive Bayes for text classification
    '''

    def __init__(self):
        '''
        Constructor
        '''
        self.categories = dict()
    
    ## {{{ http://code.activestate.com/recipes/304440/ (r1)
    def sortedDictValues(self, d, order):
        '''
        return the sorted keys given the relative entropy, the first is the smallest one, as a list [("category", relative entropy),]
        '''
        # In-place sort still works, and also has the same new features as sorted
        items = d.items()
        items.sort(key = itemgetter(1), reverse=order)
        return items
        ## end of http://code.activestate.com/recipes/304440/ }}}       
    
    def addCategory(self, catName, text):
        '''
        in: - text = text from the Document
            - catName = Name of the catogory to story in the dictionary
    
        Compute p(w), the empirical word distribution of the text. This is just the relative 
        frequency of each word, i.e., the counts of w divided by the total number of words 
        in that article. Smoothing goes later.
    
        Add the resulting q(w) to the self.categories dictionary
          
        '''
        # compute empirical distribution
        objWC = WordCount()
        
        # getting the words in the document - with preProcessing
        tokens = objWC.preProcessing(objWC.toTokens(objWC.toLowerCase(text)))
        
        print "length tokens Training Article: ", len(tokens)
        
        self.categories[catName] = tokens
    
    
    def computeNaiveBayes(self, tokensTest, catName):
        '''
        Use the Naive Bayes. Compute the following three quantities.
        '''
        # compute empirical distribution
        objWC = WordCount()
        
        # getting the words in the document - with preProcessing
        tokensTraining = self.categories[catName]
        
        # creating the union of the two words in each document
        setTest = set(tokensTest)
        setTraining = set(tokensTraining)
        setUnion = setTest.union(setTraining)
        
        # Size if the underlying vocabulary, in number of words
        vocSize = 10000
        
        # getting the dictionary of the probability mass function p(w)
        # A dictionary is the same thing as a 'associative array'
        # Here we use the smoothing method
        specialTokensTraining = setUnion.difference(setUnion.intersection(setTraining)) 
        qx_pmf = objWC.getProbabilityMassFunctionSmoothing(tokensTraining, vocSize, specialTokensTraining)
        
#        specialTokensTest = setUnion.difference(setUnion.intersection(setTest)) 
#        px_pmf = objWC.getProbabilityMassFunction(tokensTest, vocSize, specialTokensTest)
        
        # checking sum up to one
#        if abs(1.0 - sum(px_pmf.values())) > EPSILON:
#            raise ValueError('p(x) not sum up to one')
        if abs(1.0 - sum(qx_pmf.values())) > EPSILON:
            raise ValueError('q(x) not sum up to one')
        
#        # checking alphabet
#        if len(set(qx_pmf.keys()).intersection(set(px_pmf.keys()))) == 0.0:
#            raise ValueError('q(x) and p(x) have not the same alphabet')
        
        # p(D|C=c)
        pDc = 0
        
        # xi is the element i in the alphabet X
        for wi in tokensTest:
            # qxi is the probability of element xi
            qxi = qx_pmf[wi]
            pDc += math.log(qxi, 2) 
        
        nbayes = pDc + math.log(1.0/len(self.categories), 2)
        
        print "\nCat = ", catName,". Naive Bayes = ", nbayes, " bits"
        
        return nbayes
    
    
    def computeKullbackLeiblerDivergence(self, tokensTest, catName):
        '''
        Use the "add one" smoothing (i.e., Laplace smoothing) method to estimate a smoothed unigram language model, q(w), 
        based on your training article (i.e., article1). Compute the following three quantities.
        '''
        # compute empirical distribution
        objWC = WordCount()
        
        # getting the words in the document - with preProcessing
        tokensTraining = self.categories[catName]
        
        # creating the union of the two words in each document
        setTest = set(tokensTest)
        setTraining = set(tokensTraining)
        setUnion = setTest.union(setTraining)
        
        # Size if the underlying vocabulary, in number of words
        vocSize = 10000
        
        # getting the dictionary of the probability mass function p(w)
        # A dictionary is the same thing as a 'associative array'
        # Here we use the smoothing method
        specialTokensTraining = setUnion.difference(setUnion.intersection(setTraining)) 
        qx_pmf = objWC.getProbabilityMassFunctionSmoothing(tokensTraining, vocSize, specialTokensTraining)
        
        specialTokensTest = setUnion.difference(setUnion.intersection(setTest)) 
        px_pmf = objWC.getProbabilityMassFunction(tokensTest, vocSize, specialTokensTest)
        
        # checking sum up to one
        if abs(1.0 - sum(px_pmf.values())) > EPSILON:
            raise ValueError('p(x) not sum up to one')
        if abs(1.0 - sum(qx_pmf.values())) > EPSILON:
            raise ValueError('q(x) not sum up to one')
        
        # checking alphabet
        if len(set(qx_pmf.keys()).intersection(set(px_pmf.keys()))) == 0.0:
            raise ValueError('q(x) and p(x) have not the same alphabet')
        
        # Computing the entropy of p(w)
        objIT = InformationTheory()
        
        Dpq = objIT.computeKullbackLeiblerDivergence(px_pmf, qx_pmf)
        
        print "\nCat = ", catName,". Kullback-Leibler Divergence D(p||q) = ", Dpq, " bits, D(p||q) = ", Dpq/math.log(math.e, 2), " nats"
        
        return Dpq
  
    def getClassification(self, text):
        '''
        in: -text = text from the document to classified
    
        output: most likely category
        
        '''
        
        # compute empirical distribution
        objWC = WordCount()
        
        # getting the words in the document - with preProcessing
        tokens = objWC.preProcessing(objWC.toTokens(objWC.toLowerCase(text)))
        
        res = dict()
        res2 = dict()
        for cat in self.categories.iterkeys():
            res[cat] = self.computeKullbackLeiblerDivergence(tokens, cat)
            res2[cat] = self.computeNaiveBayes(tokens, cat)
            
        sres = self.sortedDictValues(res, False)
        sres2 = self.sortedDictValues(res2, True)
        
        
        if sres[0][0] == sres2[0][0]:
            return (sres2[0][0], sres2[0][1], sres[0][1])
        
        else:
            print "NO have the same category?"
            return None
        
#        print "\n\n\n" + str(sres[0])
#        print "\n" + str(sres2[0])
#        
#        return sres2[0].append()
        
            
if __name__ == '__main__':
    
    fileArtTrai1 = open("/Users/pinogal/Desktop/CPEG657Project/homegrown.txt")
    fileArtTrai2 = open("/Users/pinogal/Desktop/CPEG657Project/kates.txt")
    fileArtTest = open("/Users/pinogal/Desktop/CPEG657Project/testGrottos.txt")
    
    tra1 = fileArtTrai1.read()
    tra2 = fileArtTrai2.read()
    test = fileArtTest.read()
    
    
    obj = LanguageModel()
    obj.addCategory("Healthy", tra1)
    obj.addCategory("Unhealthy", tra2)
    
    print obj.getClassification(test)
            