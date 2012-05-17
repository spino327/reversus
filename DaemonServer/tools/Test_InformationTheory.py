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
from tools.InformationTheory import InformationTheory


class Test(unittest.TestCase):


    def testComputeEntropy(self):
        
        # from lectures, example of entropy
        pmf = dict()
        
        for i in range(8):
            pmf[i] = 1.0/8.0

        exp = 3 # bits

        objIT = InformationTheory()
        
        res = objIT.computeEntropy(pmf)
        
        self.assertEqual(exp, res)
        
        
    def testComputeCrossEntropy(self):
         
        exp = 3
        
        # from lectures, example of entropy
        px = dict()
        qx = dict()
        
        for i in range(8):
            px[i] = 1.0/8.0
            
        
        for i in range(8):
            qx[i] = 1.0/8.0
        
        obj = InformationTheory()
        
        res = obj.computeCrossEntropy(px, qx)
        
        self.assertEqual(exp, res)
        
    def testComputeKullbackLeiblerDivergence(self):
        
        # from lectures, example of entropy
        px = dict()
        qx = dict()
        
        obj = InformationTheory()
        
        for i in range(8):
            px[i] = 1.0/8.0    
        
        qx[0] = 1.0/2.0
        qx[1] = 1.0/8.0
        qx[2] = 1.0/8.0
        qx[3] = 1.0/8.0
        qx[4] = 1.0/32.0
        qx[5] = 1.0/32.0
        qx[6] = 1.0/32.0
        qx[7] = 1.0/32.0
        
        self.assertEqual(1.0, sum(qx.values()))
        
        exp = obj.computeCrossEntropy(px, qx) - obj.computeEntropy(px)
        
        res = obj.computeKullbackLeiblerDivergence(px, qx)
        
        self.assertEqual(exp, res)
        
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testComputeEntropy']
    unittest.main()