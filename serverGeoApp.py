from geoRequest import retrieveGeoInfo, retrieveSimilarAnswers 
from flask import Flask
from flask_restful import Api,Resource,reqparse   #for parse request and replies automatically                  

app = Flask(__name__)
api = Api(app) 


'''import time
start_time = time.time()


lat, long, country,locality,city= retrieveGeoInfo()

print("lat: "+lat, "long: "+long,"country: "+country, "locality: "+ locality, "city: "+city )

print("----------------------------------")
print(retrieveSimilarAnswers( country,locality))

print("--- %s seconds ---" % (time.time() - start_time))'''

trueAnswer=0
falseAnsw=1
parser = reqparse.RequestParser()

class GeoApp(Resource):    #Resource for use Crud op and other...
   
    def get(self):
        parser.add_argument('req',type=int,required = True)
        parser.add_argument('country',type=str,required = False)
        parser.add_argument('city',type=str, required = False)

        args = parser.parse_args() #parse the msg

        req = args['req']#must be setted to 0 if is required the true Answer
                         #must be setted to 1 if are required the theree other possible false Answer

        if(req == trueAnswer):
            lat, long, country,locality,city= retrieveGeoInfo()
            return {"error":False, 'msg':"return true answer", 'lat':lat,'long':long, 'responseCountry':country,'responseCity':city }
        elif(req==falseAnsw):
            if(args['country'] and args['city']):
                country = args['country']
                city = args['city']
                falseAnswers = retrieveSimilarAnswers( country,city)
                return {'error':False, 'msg':"return false answers", 
                        'fCountry1':falseAnswers[0]['country'],'fCity1':falseAnswers[0]['city'],
                        'fCountry2':falseAnswers[1]['country'],'fCity2':falseAnswers[1]['city'],
                        'fCountry3':falseAnswers[2]['country'],'fCity3':falseAnswers[2]['city']}

            else:
                 return {"error":True, 'msg':"required country and city of true Answer"}
        
        
'''
    def post(self):
        parser.add_argument('req',type=int,required = True) 
        parser.add_argument('who',type=int,required = True)
        parser.add_argument('moveX',type=int,required = True)
        parser.add_argument('moveY',type=int,required = True)
        args = parser.parse_args() #parse the msg

        req = args['req']
        who = args['who']
        moveY = args['moveY']
        moveX = args['moveX']

        parser.remove_argument('moveX')
        parser.remove_argument('moveY')

        if req == MOVE:
            lastMove[who] = [moveX,moveY]
            return {"error":False, 'msg':"MOVE MESSAGE", 'moveX':lastMove[who][0], 'moveY': lastMove[who][1]}
            
        else:
            return{"error": True, 'msg':"'req' !=2}"}


'''

api.add_resource(GeoApp,"/")

if __name__ == "__main__":
    print("starting api...")
    app.run(host = "0.0.0.0")

