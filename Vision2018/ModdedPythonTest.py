# -*- coding: utf-8 -*-
"""
Created on Sun Feb 11 09:09:06 2018

@author: Team138
"""


#copied jevois code
import libjevois as jevois
import cv2
import numpy as np

#import matplotlib.pyplot as plt

class PythonTest:
    # ###################################################################################################
    ## Constructor
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
        
    def __init__(self):
        self.morphBNo2 = 2
        
        # Instantiate a JeVois Timer to measure our processing framerate:
        self.timer = jevois.Timer("Module", 50, jevois.LOG_DEBUG)
        # Instantiate a circular blob detector:
    #    params = cv2.SimpleBlobDetector_Params()
    #    params.filterByCircularity = True
    #    params.filterByArea = True
    #    params.minArea = 200.0
    #    self.detector = cv2.SimpleBlobDetector_create(params)
    #    # Create a morpho kernel (this was not in the original code?)
        self.kernel = np.ones((5,5), np.uint8)
        
    # ###################################################################################################
    ## Process function with no USB output
    #def processNoUSB(self, inframe):
    #    jevois.LFATAL("process no usb not implemented")
    # ###################################################################################################
    ## Process function with USB output
    def process(self, inframe, outframe):
        # Get the next camera image (may block until it is captured) and convert it to OpenCV BGR (for color output):
        #img = inframe.getCvBGR()
        # Also convert it to grayscale for processing:
        #grayImage = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # Get image width, height:
        #height, width = grayImage.shape
        # Start measuring image processing time (NOTE: does not account for input conversion time):
        #self.timer.start()
        # filter noise
        #grayImage = cv2.GaussianBlur(grayImage, (5, 5), 0, 0)
        # apply automatic threshold
        #ret, grayImage = cv2.threshold(grayImage, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)
        # background area
        #grayImage = cv2.dilate(grayImage, self.kernel, iterations = 1) #self.morphBNo2)
        #invBack2 = 255 - grayImage
        # blob detection
        #keypoints = self.detector.detect(invBack2)
        #nrOfBlobs = len(keypoints)
        # draw keypoints
        #im_with_keypoints = cv2.drawKeypoints(img, keypoints, np.array([]), (255, 0, 0),
        #                                      cv2.DRAW_MATCHES_FLAGS_DRAW_RICH_KEYPOINTS)
 
        # text only appears if at least 1 blob is detected
        #if nrOfBlobs > 0:
        #    cv2.putText(im_with_keypoints, "total pips: {}".format(nrOfBlobs), (10, 25), cv2.FONT_HERSHEY_PLAIN,
        #                2, (0, 255, 255, 255))
        # Write frames/s info from our timer (NOTE: does not account for output conversion time):
        #fps = self.timer.stop()
        #cv2.putText(im_with_keypoints, fps, (3, height - 6), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
        #            (255,255,255), 1, cv2.LINE_AA)
    
        # Convert our BGR image to video output format and send to host over USB:
        #outframe.sendCvBGR(im_with_keypoints)
         # Convert to HSV
        img = inframe.getCvBGR()        
        img1 = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        
        # filter by color and intensity
    
    
    #Colors++++++++++++++++++++++++++++++++++++++++++
    #test red or blue
        redStart = True

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
        thresholded = PythonTest.adaptiveThreshold(ret,0.95)
        thresholded = PythonTest.cleanupImage(thresholded,9)    
        lightContours = PythonTest.locateContours(thresholded,0,0)
        
        
        #cnt = lightContours[0]
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
        
        foundWidth = lastx-firstx
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
    #    cv2.imshow('ret',ret)
    #    cv2.imshow('threshold',thresholded)
    
    
    #    ch = 0xFF & cv2.waitKey(100) 
        
        # sum in x and y looking the two vertical bars
        xsum = np.sum(thresholded,0)
        ysum = np.sum(thresholded,1)
    # turned to thresholded from "ret: 
        cv2.line(img,(lastx,righty),(firstx,lefty),255,2)
    #    cv2.imshow('threshold',thresholded)
        lookforPeaks = True
        targetFound = 0
        if (lookforPeaks):
            xpeaks = PythonTest.findPeaks(xsum)
            ypeaks = PythonTest.findPeaks(ysum)
            #print 'X=',xpeaks,'    Y=',ypeaks
            
            aveWidth = foundWidth/(len(xpeaks)-1)
            #print(aveWidth)
        
        
            
    
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
        
        
        
    #    if (targetFound == 1):
    #        drawCrosshair(img,xcenter,ycenter,True)
            
            
        #if (lookforPeaks):
            #plt.figure(1)
            #plt.subplot(2,1,1)
            #plt.plot(xsum)
            
            
            #plt.subplot(2,1,2)
            #plt.plot(ysum)
            
        #cv2.imshow('Input',img)
        
        #if (lookforPeaks):
            #plt.show(10)
        
        outframe.sendCvBGR(img)

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
        
    def locateContours(imageInput,xoffset,yoffset):
        """
        locate contours and apply a simple set of metrics
        to validate the contour is a target. The result is a
        list of dictionaries the represent each potential
        target.
        """
        debugPrint = False
         #Find contours and sort largest to smallest
        edged = cv2.Canny(imageInput, 30, 200)
        img1, contours, hierarchy = cv2.findContours(edged.copy(),cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
        contours = sorted(contours, key = cv2.contourArea, reverse = True)[:75]

        contours = PythonTest.removeDupContours(contours)
        if (debugPrint):    
            #print "# of No Duplicate Contours:",len(contours)
        
            cv2.drawContours(img,contours,-1,(0,255,0))
        return contours

    def cleanupImage(img,minSize):
        kernel = np.ones((minSize,minSize),np.uint8)
        ret = cv2.morphologyEx(img, cv2.MORPH_OPEN, kernel)
        return ret    
    
    def distanceToCamera(knownWidth, perWidth):
        
        return (knownWidth*722.5)/perWidth
        #camera resolution is 640/480
        #distance: 17in, width: 16in, pixels: 680
        #F=(680*17)/16 = 722.5
        
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
                if arr[k] > maxVal / 2.8:
                    looking = False
                    start = k
            else:
                if arr[k] < maxVal / 4:
                    looking = True
                    ret.append((start,k))
        if not looking:
            ret.append((start,len(arr)-1))
            
        return ret
   
