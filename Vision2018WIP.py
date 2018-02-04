# -*- coding: utf-8 -*-
"""
Created on Fri Jan 27 12:47:59 2017

@author: Jeffrey Bryant
"""
from os import listdir
from os.path import isfile, join
import cv2
import numpy as np
import matplotlib.pyplot as plt


debugPrint = True;

    
def drawCrosshair(img,x,y,match):
    """
    Draw a crosshair on the image at the specified coordinates
    """    
    size = 21
    if (match):
        color = (0,0,255)
    else:
        color = (0,255,255)
    thickness = 2
    
    cv2.line(img,(x-size,y),(x+size,y),color,thickness)
    cv2.line(img,(x,y-size),(x,y+size),color,thickness)
    for k in range(4):
        cv2.circle(img,(x,y),k*size/3,color,1)    

nColors = 4 
def adaptiveThreshold(img,percentile):
    """
    Threshold an image by producting a histogram of the intensities
    and then a cumulative array and setting the threshold at the
    percentile value (fraction between 0 and 1.0)
    """
    sums,bins = np.histogram(img.ravel(),256)
    cumsums = np.cumsum(sums)
    threshold = 0
    limit = cumsums[255] * percentile
    for k in cumsums:
        if k < limit:
            threshold = threshold + 1
        else:
            break
    ret,img1 = cv2.threshold(img,threshold,255,cv2.THRESH_BINARY)
    return img1

def cleanupImage(img,minSize):
    """
    cleanup an image removing low level noise
    """
    kernel = np.ones((minSize,minSize),np.uint8)
    ret = cv2.morphologyEx(img, cv2.MORPH_OPEN, kernel)
    return ret
    
def processImage(img):
    
   
    # Convert to HSV
    img1 = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    
    # filter by color and intensity


#Colors++++++++++++++++++++++++++++++++++++++++++
#test red or blue
    redStart = True
    findScale = False
#blue
    if not (redStart):
        lower_blue = np.array([99,100,100])
        upper_blue = np.array([129,255,255])
        mask = cv2.inRange(img1, lower_blue, upper_blue)
#Red
    if (redStart):
        lower_red = np.array([0,100,100])
        upper_red = np.array([10,255,255])
        mask = cv2.inRange(img1, lower_red, upper_red)
       
        lower_red = np.array([160,100,100])
        upper_red = np.array([179,255,255])
        mask1 = cv2.inRange(img1, lower_red, upper_red)
    
        mask = cv2.bitwise_or(mask,mask1)
    kernel = np.ones((7,7), np.uint8)
    mask = cv2.dilate(mask,kernel,iterations = 5)
    # mask off the grayscale image
    gray = img1[:,:,2]
    ret = cv2.bitwise_and(gray,gray, mask= mask)
    thresholded = adaptiveThreshold(ret,0.95)
    thresholded = cleanupImage(thresholded,9)    
    lightContours = locateContours(thresholded,0,0)
    
    
#    cnt = lightContours[0]
    patchwork = lightContours[0]
    for cnt in lightContours:
        patchwork = np.concatenate((patchwork,cnt))
    patchwork = np.asarray(patchwork)
    [vx,vy,x,y] = cv2.fitLine(patchwork,cv2.DIST_L2,0,1.0,0.99)
    firstx = 490
    lastx = 0
    for piece in patchwork:
       tidBit = piece[0]
       if tidBit[0]<firstx:
           firstx = tidBit[0]
       if tidBit[0]>lastx:
           lastx = tidBit[0]
    
    lefty = int((-x*vy/vx) + y)
    righty = int(((gray.shape[1]-x)*vy/vx)+y)
#    rightx = 
#    leftx
#    cv2.line(thresholded,(gray.shape[1]-1,righty),(0,lefty),255,2)
#    cv2.line(thresholded,(lastx,righty),(firstx,lefty),255,2)



  
#BOOK MARK, XY IS THE MIDDLE OF THE FITTED LINE


#    whatsLeft=[]
#    points = np.array([])
#    for cnt in lightContours:
#        x,y,w,h = cv2.boundingRect(cnt)
#        cv2.rectangle(img,(x,y),(x+w,y+h),(0,255,0),2)
#        centerx = x+(w/2)
#        centery = y-(h/2)
#        points = np.concatenate((points, [centerx, centery]))
#    fittedLine = cv2.fitLine(points, cv2.DIST_L2, 0, 0.01, 0.01)
    


#        cv2.imwrite('hough;ines3.jpg',img)
    cv2.imshow('ret',ret)
    cv2.imshow('threshold',thresholded)

    
    ch = 0xFF & cv2.waitKey(1000) 
    
    # sum in x and y looking the two vertical bars
    xsum = np.sum(ret,0)
    ysum = np.sum(ret,1)
    
    lookforPeaks = True
    targetFound = 0
    if (lookforPeaks):
        xpeaks = findPeaks(xsum)
        ypeaks = findPeaks(ysum)
        print 'X=',xpeaks,'    Y=',ypeaks
        
        

#        if len(xpeaks) == 1  and len(ypeaks) > 0:
#            xend = len(xpeaks)-1
#            xcenter = xpeaks[0][0] + (xpeaks[xend][1]-xpeaks[0][0]) / 2
#            yend = len(ypeaks)-1
#            ycenter = ypeaks[0][0] + (ypeaks[yend][1]-ypeaks[0][0]) / 2
#            targetFound = 1
#        elif len(xpeaks)==2 and len(ypeaks) >0:
#            xend = len(xpeaks)-1
#            xcenter = xpeaks[0][1] + (xpeaks[1][0]-xpeaks[0][1]) / 2
#            yend = len(ypeaks)-1
#            ycenter = ypeaks[0][0] + (ypeaks[yend][1]-ypeaks[0][0]) / 2
#            targetFound = 1
    
    #testing things
    #grayContours = locateContours(ret,0,0)
    #cv2.imshow()
    
    
#    contours = locateContours(img,0,0)  
    if (targetFound == 1):
        drawCrosshair(img,xcenter,ycenter,True)
        
        
    if (lookforPeaks):
        plt.figure(1)
        plt.subplot(2,1,1)
        plt.plot(xsum)
        
        
        plt.subplot(2,1,2)
        plt.plot(ysum)
        
    cv2.imshow('Input',img)
    
    if (lookforPeaks):
        plt.show(10)
    
    return ret
    
    

    

def thresholdImage(img):
    pass

def findGearTarget(img):
    pass

def findHighGoalTarget(img):
    pass

def findPeaks(arr):
    ret = []
    looking = True
    start = 0
    maxVal = max(arr)
    for k in range(len(arr)):
        if looking:
            if arr[k] > maxVal / 2:
                looking = False
                start = k
        else:
            if arr[k] < maxVal / 4:
                looking = True
                ret.append((start,k))
    if not looking:
        ret.append((start,len(arr)-1))
        
    return ret
    
def removeDupContours(inContours):
    """
    Remove duplicates that start at the same location and have the same bounds
    """
    if len(inContours) == 0:
        return []
    ret = []
    last = inContours[0]
    ret.append(last)
    lastx,lasty,lastw,lasth = cv2.boundingRect(last)
    for c in inContours:
        x,y,w,h = cv2.boundingRect(c)
        if (x != lastx) or (y != lasty) or (h != lasth) or (w != lastw):
            ret.append(c);
            last = c;
            lastx,lasty,lastw,lasth = cv2.boundingRect(last)
    return ret    
    
def locateContours(img,xoffset,yoffset):
    """
    locate contours and apply a simple set of metrics
    to validate the contour is a target. The result is a
    list of dictionaries the represent each potential
    target.
    """
    
     #Find contours and sort largest to smallest
    edged = cv2.Canny(img, 30, 200)
    img1, contours, hierarchy = cv2.findContours(edged.copy(),cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
    contours = sorted(contours, key = cv2.contourArea, reverse = True)[:75]
    if (debugPrint):
        print "Contours:",len(contours)
    contours = removeDupContours(contours)
    if (debugPrint):    
        print "# of No Duplicate Contours:",len(contours)
    
        cv2.drawContours(img,contours,-1,(0,255,0))
    return contours
    
                

def processDirectory(imgdir):
    """
    Perform an analysis on all the .jpg files in a directory
    """
    
    onlyfiles = [f for f in listdir(imgdir) if isfile(join(imgdir, f))]
    for fn in onlyfiles:
        path = join(imgdir, fn)
        if fn[-4:].lower() == '.jpg':
            imgInput = cv2.imread(path)
            imgOutput =  cv2.resize(imgInput,None,fx=0.1, fy=0.1, interpolation = cv2.INTER_CUBIC)
            imgInput = imgOutput
            
            ch = 0xFF & cv2.waitKey(1000)
            
            if ch == 27:
                break
            
            processed = processImage(imgInput)
            
            
  

if __name__ == '__main__':
    """
    Main test program
    """
    print "OpenCV Version:",cv2.__version__
    
#    processDirectory("C:\\Users\\Team138\\Documents\\Team 138\\vision 2018\\FIRST PowerUp Field Pics\\FIRST PowerUp Field Pics")
    processDirectory("C:/Users/Team138/Documents/Team 138/vision 2018/FIRST PowerUp Field Pics/Red Scale pics")
    #processDirectory("LED Boiler")
    #processDirectory("Red Boiler")
    #processDirectory("Blue Boiler")
    

