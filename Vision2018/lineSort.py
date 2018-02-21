# -*- coding: utf-8 -*-
"""
Created on Mon Feb 19 09:02:05 2018

 Module to locate light strings using center points. The main function
 computeClusters groups the points equally spaced along a line. The slopeThreshold
 and spacingThreshold may be used to adjust the sensitivity of the clustering
 
@author: jeffrey.f.bryant
"""

from __future__ import print_function
from cmath import rect, phase
from math import radians, degrees,sqrt,atan2,pi

# Clustering parameter tweakable threshold constants
slopeThreshold = 10.0 # degrees
spacingThreshold = 0.3  # percentage as a fraction


class Point:
    """
    Single point object
    """
    
    def __init__(self,x,y):
        self.x = x
        self.y = y
        
    def sameLocation(self,other):
        if (self.x != other.x):
            return False
        if (self.y != other.y):
            return False
        return True
        
    def toPolar(self,other):
        """
        Compute the distance and angle(slope) to another point.
        """
        dx = other.x-self.x
        dy = other.y-self.y
        return (sqrt(dx*dx+dy*dy),(180.0 * atan2(dy,dx)/pi))
        
         
class Pair:
    """
    pair of points used to measure distance and slope
    """
    def __init__(self,p1,p2):
        
        self.p1 = p1
        self.p2 = p2
        pol = p1.toPolar(p2)
        self.dist = pol[0]
        self.slope = pol[1]
        
    def deltaAngle(self,other):
        deltaSlope = self.slope-other.slope
        if (deltaSlope < 0.0):
            deltaSlope = -deltaSlope
        if (deltaSlope > 180.0):
            deltaSlope = 360 - deltaSlope
        return deltaSlope
        
def meanAngle(deg):
    """
    Compute the mean of a list of angles in degrees. The computations
    are done in cartesian coordinates to avoid the circle wrap discontinuity 
    """
    return degrees(phase(sum(rect(1, radians(d)) for d in deg)/len(deg)))

class Cluster:
    """
    Cluster is a container for 3 or more points along a line that are
    spaced evenly.
    """
    def __init__(self,p1,p2,p3) :
        
        self.valid = False
            
        pair1 = Pair(p1,p2)
        pair2 = Pair(p2,p3)
        pair3 = Pair(p1,p3)
        
        # Must be unique points
        if (pair1.dist == 0.0):
            return
        if (pair2.dist == 0.0):
            return
        if (pair3.dist == 0.0):
            return

        # test if all three points a colinear (same slope)    
        delta1 = pair1.deltaAngle(pair2)        
        if (delta1 > slopeThreshold):
            return
            
        delta2 = pair2.deltaAngle(pair3)
        if (delta2 > slopeThreshold):
            return
            
        delta3 = pair1.deltaAngle(pair3)
        if (delta3 > slopeThreshold):
            return
            
        # Compute spacing between point pairs
        largestDist = pair1
        if (pair2.dist > largestDist.dist):
            largestDist = pair2
        if (pair3.dist > largestDist.dist):
            largestDist = pair3
            
        if (largestDist != pair1):
            close1 = pair1.dist
        if (largestDist != pair2):
            close2 = pair2.dist
        else:
            close2 = pair3.dist
            
        # Check they are spaced evenly
        self.avgDist = (close1 + close2) / 2.0;
        if (abs(close1-self.avgDist) > spacingThreshold):
            return

        self.avgSlope = meanAngle([pair1.slope,pair2.slope,pair3.slope])
        self.points = [p1,p2,p3]
        self.valid = True
        return
        
        
    def addPoint(self,newPoint):
        """
        Add a point to the cluster if it meets the slope and spacing criteria.
        A True return indicates the point is added and averages updated. False 
        indicates the point was rejected.
        """
        closestDist = 10000.0
    
        # compute average slope and find the closest point
        slopes = []
        for p in self.points:
            newPair = Pair(p,newPoint)
            if (newPair.dist < closestDist):
                closestDist = newPair.dist
                slopes.append(newPair.slope)
                closestSlope = newPair.slope
        
                
        # make shure the slope is within bounds
        deltaSlope = abs(self.avgSlope - closestSlope)
        if (deltaSlope > 180.0):
            deltaSlope = deltaSlope - 180.0
        if (deltaSlope > slopeThreshold):
            return False
    
        # make shure the spacing is within specification
        if (abs(closestDist-self.avgDist)/self.avgDist > spacingThreshold):
            return False
        
        # made it this far; add the point and update the averages
        beta = len(self.points)
        betaP1 = beta + 1.0;
        self.avgDist = self.avgDist * beta / (betaP1) + closestDist / (betaP1)
        self.avgSlope = meanAngle(slopes)
        if (self.avgSlope < -90.0):
            self.avgSlope = self.avgSlope + 180.0
        if (self.avgSlope > 90.0):
            self.avgSlope = self.avgSlope - 180.0
        self.points.append(newPoint)
        
        return True
        
def addMorePoints(cluster,points,pointsToRemove):
    """
    Attempt to grow a cluster once it has been created by appending points
    to it
    """
    pointAdded = True
    while pointAdded:
        pointAdded = False
        for p in points:
            if not p in pointsToRemove:
                added = cluster.addPoint(p)
                if added:
                    pointAdded = True
                    pointsToRemove.append(p)

                

def computeClusters(points):
    """ Compute line clusters for a bunch of input points by attempting to
    group them 3 at a time into clusters then add the rest
    
    TODO: Handle crossing lines better (first wins)
    
    """
    
    # Construct clusters
    clusters = []
    progress = True
    while (progress):
        pointsToRemove = []
        progress = False
        for l1 in points:
            for l2 in points:
                for l3 in points:
                    newPoint = True
                    if l1 in pointsToRemove:
                        newPoint = False
                    if l2 in pointsToRemove:
                        newPoint = False
                    if l3 in pointsToRemove:
                        newPoint = False
                    if (newPoint):
                        cluster1 = Cluster(l1,l2,l3)
                        if (cluster1.valid):
                            pointsToRemove.append(l1)
                            pointsToRemove.append(l2)
                            pointsToRemove.append(l3)
                            clusters.append(cluster1)
                            progress = True
                            
                            addMorePoints(cluster1,points,pointsToRemove)
                            print("Added Cluster Size=", len(clusters), "Points= ",len(cluster1.points))
                            break
                        
        for p in pointsToRemove:
            try:
                points.remove(p)
            except:
                pass
                        
    # attempt to add waht is left to the clusters
    for p in points:
        for c in clusters:
            c.addPoint(p)
            
    # Return the clusters
    return clusters      


# <<<<<<<<<<<<<<<<<<<<<<<<<<<<< Test Routines >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

def pairTest():
    
    print ("\n-------------------  pairTest(): ------------------")
    p1 = Point(1.1,-1.0)
    p2 = Point(2.0,-2.0)
    pair1 = Pair(p1,p2)
    print (pair1.dist,pair1.slope)
    
    p3 = Point(3.0,-3.0)
    pair2 = Pair(p2,p3)   
    print (pair2.dist,pair2.slope)
    
    print ("deltra Angle:",pair1.deltaAngle(pair2))
    
def simpleClusterTest():
    
    print ("\n-------------------  simpleClusterTest(): ------------------")
    p1 = Point(1.1,-1.0)
    p2 = Point(2.0,-2.0)
    p3 = Point(3.0,-3.0)    
    cluster1 = Cluster(p1,p2,p3)
    print (cluster1.valid,cluster1.avgDist,cluster1.avgSlope)
    
    p4 = Point(4.0,-4.1)
    added = cluster1.addPoint(p4)
    print (added,cluster1.avgDist,cluster1.avgSlope)
    
    p5 = Point(0.0,0.0)
    added = cluster1.addPoint(p5)
    print (added,cluster1.avgDist,cluster1.avgSlope)
    
    p6 = Point(5.0,-5.2)
    added = cluster1.addPoint(p6)
    print (added,cluster1.avgDist,cluster1.avgSlope)
    
    p7 = Point(-1.0,+1.0)
    added = cluster1.addPoint(p7)
    print (added,cluster1.avgDist,cluster1.avgSlope)
    
    # Should not work (not on the line)
    p8 = Point(-1.0,-1.0)
    added = cluster1.addPoint(p8)
    print (added,cluster1.avgDist,cluster1.avgSlope)

    for p in cluster1.points:
        print (p.x,p.y)
        
        
def printClusters(clusters):
    for c in clusters:
        print ("Cluster [valid,slope,avgDistance]:",c.valid,c.avgSlope,c.avgDist)
        for p in c.points:
            print ("    ",p.x,p.y)
    
                
def computeClustersTest():
    
    print ("\n-------------------  computeClustersTest(): ------------------")
    
    p1 = Point(0.0,0.0)
    p2 = Point(1.0,1.0)
    p3 = Point(2.0,2.0)
    p4 = Point(3.0,3.0)
    
    p5 = Point(-1.0,0.0)
    p6 = Point(-2.0,1.0)
    p7 = Point(-3.0,2.0)
    p8 = Point(-4.0,3.0)

    points = [p1,p2,p3,p4,p5,p6,p7,p8]
    clusters = computeClusters(points)
    printClusters(clusters)
            
if __name__ == '__main__': 
    """
    Unit test programs
    """           
    pairTest()
    simpleClusterTest()
    computeClustersTest()