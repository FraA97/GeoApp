from geoRequest import randomizeLocation, retrieveGeoInfo, retrieveSimilarAnswers 
from flask import Flask
from flask_restful import Api,Resource,reqparse   #for parse request and replies automatically                  

app = Flask(__name__)
api = Api(app) 

trueAnswer=0
falseAnsw=1
parser = reqparse.RequestParser()

class GeoApp(Resource):    #Resource for use Crud op and other...
   
    def get(self):
        parser.add_argument('req',type=int,required = True)
        parser.add_argument('country',type=str,required = False)
        parser.add_argument('city',type=str, required = False)
        parser.add_argument('level',type=int, required = False)

        args = parser.parse_args() #parse the msg

        req = args['req']#must be setted to 0 if is required the true Answer
                         #must be setted to 1 if are required the theree other possible false Answer
        level = args['level']
        if(req == trueAnswer):
            lat, long, country,city = retrieveGeoInfo(level,1,None,0)
            lat, long = randomizeLocation(lat,long)
            return {"error":False, 'msg':"return true answer", 'lat':lat,'long':long, 'responseCountry':country,'responseCity':city }
        elif(req==falseAnsw):
            if(args['country'] and args['city']):
                country = args['country']
                city = args['city']
                f_lat,f_long,false_country,false_city = retrieveGeoInfo(level,3,country,0) 
                falseAnswers = retrieveSimilarAnswers(country,city,false_country,false_city)
                #falseAnswers = retrieveSimilarAnswers( country,city)
                return {'error':False, 'msg':"return false answers", 
                        'fCountry1':falseAnswers[1]['country'],'fCity1':falseAnswers[1]['city'],
                        'fCountry2':falseAnswers[2]['country'],'fCity2':falseAnswers[2]['city'],
                        'fCountry3':falseAnswers[3]['country'],'fCity3':falseAnswers[3]['city']}

            else:
                 return {"error":True, 'msg':"required country and city of true Answer"}
        

api.add_resource(GeoApp,"/")

if __name__ == "__main__":
    print("starting api...")
    #app.run(host = "0.0.0.0")
    from waitress import serve
    serve(app, host="0.0.0.0", port=5000)