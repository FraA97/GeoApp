import requests
import random
import json
import time
import xml.etree.ElementTree 

def retrieveGeoInfo():
    lat, long, country, locality, city='','','','',''
    okResp= False
    totTime=0
    while(not okResp): #filter zones with only sea
        latRange =[-180,+180]
        lonRange =[-60,+70]
        start_time = time.time()
        lat,long,country,city = chooseGoodCoordinates(latRange,lonRange)
        totTime+=time.time() - start_time
        return lat,long,country,locality,city 
        
def retrieveSimilarAnswers(sourceCountry, sourceLocality):
    answers=[]
    
    i=0
    while(i<3):
        lt, lg, country,locality, city = retrieveGeoInfo()
        if( sourceCountry!=country):
            answers.append({'country':country,'locality':locality, 'city':city})
            i+=1
        
    return answers

def chooseGoodCoordinates(latRange,lonRange):
    lat,long,country,city ='','','',''
    goodCoord=False
    #filter coordinates
    while(not goodCoord):
            partialLat= random.uniform(latRange[0],latRange[1])
            partialLong = random.uniform(lonRange[0],lonRange[1])
            if( ( partialLat>-45 and partialLong<-40  ) #remove southern ocean
                    or  (partialLat< -82 and partialLong<+7)  #remove south ocean pacific
                    or  (partialLat>+50 and partialLat<+105 and partialLong>=-40 and partialLong<-7)  #remove indian ocean
                    or  (partialLat<-125 and  partialLong<54 and partialLong>+7)#remove north pacific ocean
                    or  (partialLat>+127 and partialLong>+0 and partialLong< +30)#remove pacific ocean
                    or  (partialLat>+150 and partialLong>+30 and partialLong< +60)#remove pacific ocean
                    or  (partialLat>-45 and partialLat<+14 and partialLong<-22 and partialLong>=-40)#remove south Atlantic ocean 
                    or  (partialLat>-35 and partialLat<+8 and partialLong>=-22 and partialLong<2)#remove south Atlantic ocean 
                    or  (partialLat>-50 and partialLat<-17 and partialLong>+0 and partialLong<+15 )#remove north Atlantic ocean
                    or  (partialLat>-60 and partialLat<-17 and partialLong>+8 and partialLong<+45 )#remove north Atlantic ocean 
                    or  (partialLat>-53 and partialLat<-9 and  partialLong>=+45 and partialLong<+60 )#remove north Atlantic ocean 
                    or  (partialLat>+75 and  partialLong>=+55)#remove part of russia
                      ): 
                goodCoord= False

            else:
                goodCoord = True
    #SEND REQUESTS UNTIL RETURN A RESPONSE
    count=0
    marginLat=10; marginLong = 5
    while(country == '' or city == ''):
        if(count>0): marginLat*=2; marginLong*=2
        if(partialLat<= 180-marginLat and partialLat>=-180+marginLat):
            east = partialLat+marginLat
            west = partialLat-marginLat
        elif(partialLat< 180-marginLat and partialLat<-180+marginLat):
            east = -180+ marginLat*2
            west = -180
        elif(partialLat> 180-10 ): 
            east = 180 
            west = 180- marginLat*2
        if(partialLong<=70-marginLong and partialLong>=-60+marginLong): 
            north= partialLong+marginLong
            south =partialLong-marginLong
        elif(partialLong<=70-marginLong and partialLong<-60+marginLong): 
            north= -60 +marginLong*2
            south =-60
        elif(partialLong>70-marginLong): 
            north= 70
            south =70 - marginLong*2

        northStr = str("%.6f" % north)
        southStr = str("%.6f" % south)
        eastStr = str("%.6f" % east)
        weastStr = str("%.6f" % west)
        print(northStr+' '+southStr+' '+weastStr+' '+eastStr)
        r =requests.get('http://api.geonames.org/cities?north='+northStr+'&south='+southStr+'&east='+eastStr+'&west='+weastStr+'&maxRows=1'+'&username=fraart')
        
        #http://api.geonames.org/countrySubdivision?lat='+lat+'&lng='+long+'&maxRows=1&radius=40&username=fraart'
        #http://api.geonames.org/cities?north=44.1&south=-9.9&east=-22.4&west=55.2&username=demo
        #https://api.3geonames.org/?randomland=yes

        resp =r.content
        #print(resp)
        tree = xml.etree.ElementTree.fromstring(resp)
        

        for child in tree.iter('*'):
            #print(child)
            if(child.tag=="countryName"): country= child.text
            if(child.tag=="name"): city= child.text
            if(child.tag=="lat"): lat= child.text
            if(child.tag=="lng"): long= child.text 
        count+=1

    return lat,long,country, city

   
    

