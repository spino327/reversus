'''
Created on Mar 16, 2012

@author: Sergio Pino
'''

from operator import itemgetter

import re

class WordCount(object):
    '''
    Utilities for word counting
    '''

    def __init__(self):
        '''
        Constructor
        '''
        
    def toLowerCase(self, text):
        '''
        Convert all capital letter to lower cases
        '''
        return str(text).lower()
    
    def toTokens(self, text):
        '''
        Split the words on a line so that each line has only one word
        '''
        return str(text).split()
   
    ## {{{ http://code.activestate.com/recipes/304440/ (r1)
    def sortedDictValues(self, d):
        '''
        return the sorted keys given the count, as a list [("term", frequency),]
        '''
        # In-place sort still works, and also has the same new features as sorted
        items = d.items()
        items.sort(key = itemgetter(1), reverse=True)
        return items
        ## end of http://code.activestate.com/recipes/304440/ }}}     
        
    def getCount(self, tokens):
        '''
        This method get the counts of each term
        return a dictionary ("term":frequency)
        '''
        
        if type(tokens) == list:
            
            # Create a set of elements, so that we have the individual terms
            setElements = set(tokens)
            # count the frequency of each term
            dictFreq = dict()
            
            for elem in setElements:
                dictFreq[elem] = tokens.count(elem)
            
            return dictFreq
            
        else:
            return None
        
    def getProbabilityMassFunction(self, tokens, size, specialTokens = None):
        '''
        tokens
        size =  sizeof the vocabulary
        specialTokens = special tokens we want on the list of p(w_i) = 0
        
        This method get the probability mass function of the given tokens
        return a dictionary ("term":probability) with number of terms equal = size
        '''
        
        if type(tokens) == list:
            
            # Create a set of elements, so that we have the individual terms
            setElements = set(tokens)
            
            # total number of words(terms, tokens) in the document
            numWords = float(len(tokens))
            
            # count the frequency of each term
            dictPmf = dict()
            
            for elem in setElements:
                dictPmf[elem] = float(tokens.count(elem))/numWords
                
                # check the values
                if dictPmf[elem] < 0 or dictPmf[elem] > 1:
                    raise ValueError("the probability assigned is ill: p(" + str(elem) + ") = " + str(dictPmf[elem]))
            
            # filling with fake words for get len(dict) = size
            # assigning probability zero
            
            # from special tokens
            if specialTokens != None and type(specialTokens) == set:
                
                if len(setElements.intersection(specialTokens)) == 0:
                    for elem in specialTokens:
                        dictPmf[elem] = 0.0
                        
                else:
                    print "i: filling with fake words, specialTokens ignored because is not disjoint from tokens"
            else:
                print "i: filling with fake words, specialTokens ignored due to incorrect type"
            
            # fake
            for i in range(int(size - len(dictPmf))):
                dictPmf["fakeWord" + str(i)] = 0.0
            
            return dictPmf
            
        else:
            return None
        
    def getProbabilityMassFunctionSmoothing(self, tokens, size, specialTokens = None):
        '''
        tokens
        size =  sizeof the vocabulary
        specialTokens = special tokens we want on the list of p(w_i) = 0
        
        This method get the probability mass function of the given tokens with the Laplace smoothing
        return a dictionary ("term":probability) with number of terms equal = size
        '''
        
        if type(tokens) == list:
            
            # Create a set of elements, so that we have the individual terms
            setElements = set(tokens)
            
            # total number of words(terms, tokens) in the document
            numWords = float(len(tokens))
            
            # count the frequency of each term
            dictPmf = dict()
            
            for elem in setElements:
                dictPmf[elem] = float(tokens.count(elem) + 1.0)/(numWords + size)
                
                # check the values
                if dictPmf[elem] < 0 or dictPmf[elem] > 1:
                    raise ValueError("the probability assigned is ill: p(" + str(elem) + ") = " + str(dictPmf[elem]))
            
            # filling with fake words for get len(dict) = size
            # assigning probability zero
            
            # From special tokens
            if specialTokens != None and type(specialTokens) == set:
                
                if len(setElements.intersection(specialTokens)) == 0:
                    for elem in specialTokens:
                        dictPmf[elem] = float(0.0 + 1.0)/(numWords + size)
                        
                else:
                    print "i: filling with fake words, specialTokens ignored because is not disjoint from tokens"
            else:
                print "i: filling with fake words, specialTokens ignored due to incorrect type"
            
            # Fake
            for i in range(int(size - len(dictPmf))):
                dictPmf["fakeWord" + str(i)] = float(0.0 + 1.0)/(numWords + size)
            
            return dictPmf
            
        else:
            return None
        
    def preProcessing(self, text):
        '''
        Remove all special characters, punctuation and spaces from a string
        '''
        if type(text) == str:
            tokens = text.split()
            text = ""
            for elem in tokens:
                text += re.sub('[^A-Za-z0-9]+', '', elem) + " "
            
            return text.strip()
        
        elif type(text) == list:
            for i in range(len(text)):
                if not text[i].isalnum():
                    text[i] = re.sub('[^A-Za-z0-9]+', '', text[i])
            
            while text.count("") > 0:
                text.remove("")
        
            return text
        