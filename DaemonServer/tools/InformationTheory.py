'''
Created on Mar 17, 2012

@author: Sergio Pino
'''

import math

EPSILON = 0.00000000001

class InformationTheory(object):
    '''
    Information theory tools
    '''


    def __init__(self):
        '''
        Constructor
        '''
    
    def computeEntropy(self, pmf):
        '''
        Compute the Entropy (in bits) of pmf (p(x)) and return a float value. 
        We use the logarithm in base 2. 
        ''' 
        # Entropy of the R.V. X
        Hx = 0
        # getting an iterator over the values in p(x)
        px = pmf.itervalues()
        
        if abs(1.0 - sum(pmf.values())) > EPSILON:
            raise ValueError('p(x) not sum up to one')
        
        for pxi in px:
            # pxi is the probability of element xi
            if pxi > 0:
                Hx += - (pxi * math.log(pxi, 2))
        
        return Hx
        
    def computeCrossEntropy(self, px_pmf, qx_pmf):
        '''
        Compute the Cross Entropy (in bits) of px and qx, and return a float value.
        We use the logarithm in base 2.
        
        We take care of the alphabet.
        '''
        # check the sizes
        if len(px_pmf) != len(qx_pmf):
            raise ValueError('p(x) and q(x) must have the same length')
        
        if abs(1.0 - sum(px_pmf.values())) > EPSILON:
            raise ValueError('p(x) not sum up to one')
        
        if abs(1.0 - sum(qx_pmf.values())) > EPSILON:
            raise ValueError('q(x) not sum up to one')
        
        # Croos Entropy of p(x) and q(x)
        Hpq = 0

        # getting an iterator over the keys in p(x), alphabet
        x = px_pmf.keys()
        
        # xi is the element i in the alphabet X
        for xi in x:
            # qxi is the probability of element xi
            qxi = qx_pmf[xi]
            # pxi is the probability of element xi
            pxi = px_pmf[xi]
            if qxi > 0:
                Hpq += - (pxi * math.log(qxi, 2))           
       
        return Hpq
        
    def computeKullbackLeiblerDivergence(self, px_pmf, qx_pmf):
        '''
        Compute the Kullback-Leibler Divergence D(p||q) (in bits) of px and qx, and return a float value.
        We use the logarithm in base 2.
        
        We take care of the alphabet.
        '''
        # check the sizes
        if len(px_pmf) != len(qx_pmf):
            raise ValueError('p(x) and q(x) must have the same length')
        
        if abs(1.0 - sum(px_pmf.values())) > EPSILON:
            raise ValueError('p(x) not sum up to one')
        
        if abs(1.0 - sum(qx_pmf.values())) > EPSILON:
            raise ValueError('q(x) not sum up to one')
        
        # D(p||q) of p(x) and q(x)
        Dpq = 0

        # getting an iterator over the keys in p(x), alphabet
        x = px_pmf.keys()
        
        # xi is the element i in the alphabet X
        for xi in x:
            # qxi is the probability of element xi
            qxi = qx_pmf[xi]
            # pxi is the probability of element xi
            pxi = px_pmf[xi]
            if qxi > 0 and pxi > 0:
                Dpq += (pxi * math.log(pxi/qxi, 2))
       
        return Dpq
        
        