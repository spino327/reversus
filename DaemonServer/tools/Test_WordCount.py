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
import unittest

from tools.WordCount import WordCount


class Test(unittest.TestCase):


    def testToLowerCase(self):
        
        src = "TO LOWER CASE"
        exp = "to lower case"
        
        obj = WordCount()
        
        res = obj.toLowerCase(src)
        
        self.assertEqual(exp, res)
        
    def testTokenizer(self):
        
        src = "TO LOWER CASE"
        exp = ["to", "lower", "case"]
        
        obj = WordCount()
        
        res = obj.toTokens(obj.toLowerCase(src))
        
        self.assertEqual(exp, res)
        
    def testCount(self):
        
        src = "TO LOWER CASE TO LOWER CASE TO LOWER CASE TO LOWER CASE TO LOWER"
        exp = {"to":5, "lower":5, "case":4}

        obj = WordCount()
        
        tokens = obj.toTokens(obj.toLowerCase(src))
        
        res = obj.getCount(tokens)
        
        self.assertEqual(exp, res)
        
    def testSortedDictValues(self):
        
        src = "TO LOWER CASE TO LOWER CASE TO LOWER CASE TO LOWER TO"
        exp = [("to", 5), ("lower", 4), ("case", 3)]

        obj = WordCount()
        
        tokens = obj.toTokens(obj.toLowerCase(src))
        
        res = obj.sortedDictValues(obj.getCount(tokens))

        self.assertEqual(exp, res)
        
    def testGetProbabilityMassFunction(self):
        
        sizeVoc = 20.0
        src = "TO LOWER CASE TO LOWER CASE TO LOWER CASE TO LOWER TO"
        numWords = float(len(src.split()))
        exp = {"to": 5.0/numWords, "lower": 4.0/numWords, "case": 3.0/numWords}
        
        for i in range(int(sizeVoc - len(exp))):
                exp["fakeWord" + str(i)] = 0.0
        
        self.assertEquals(1.0, sum(exp.values()))
        
        expsum = 0.0
        for element in exp.values():
            expsum += element
        
        obj = WordCount()    
        
        res = obj.getProbabilityMassFunction(obj.toTokens(obj.toLowerCase(src)), sizeVoc)  
        
        self.assertEqual(sizeVoc, len(res), "Have not the same length")
          
        ressum = 0.0
        for element in res.values():
            ressum += element
        
        self.assertEquals(1.0, sum(res.values()), "p(x) must sum up to one")
        #self.assertDictEqual(exp, res, "Dicts not equals")
        self.assertEquals(expsum, ressum, "The sums were not equals")

    def testPreProcessing(self):
        
        src = 'human champions of the "Jeopardy!" television show,'
        
        exp = "human champions of the Jeopardy television show"
        
        obj = WordCount()
        
        res = obj.preProcessing(src)
        
        self.assertEqual(exp, res)

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test']
    unittest.main()