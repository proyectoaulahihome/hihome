import cv2
import os
import sys
import json
from flask import Flask,jsonify, request

app = Flask(__name__)


class reconocimiento:
    def __init__(self, ruta):
        self.ruta = ruta
    def reconocimientousuario(self):
        absolutepath = os.path.abspath(__file__)
        fileDirectory = os.path.dirname(absolutepath)
        parentDirectory = os.path.dirname(fileDirectory)
        dataPath = os.path.join(parentDirectory, 'Data')
        #print(dataPath)
        #dataPath = 'G:/Mi unidad/2021/Universidad/8vo/Aplicaciones distribuidas/Proyecto_Aula/Data'
        imagePaths = os.listdir(dataPath.replace('\\', '/'))
        #print('imagePaths=', imagePaths)
        #Leyendo el modelo LBPH
        face_recognizer = cv2.face.LBPHFaceRecognizer_create()
        face_recognizer.read('reconocimiento_Facial/Modelos/modeloLBPHFace.xml')
        #cap = cv2.VideoCapture(self.ruta)
        #Probandoe en tiempo real
        cap = cv2.VideoCapture(0,cv2.CAP_DSHOW)
        faceClassif = cv2.CascadeClassifier(cv2.data.haarcascades+'haarcascade_frontalface_default.xml')
        lista=[]
        while True:
            ret, frame = cap.read()
            if ret == False: break
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            auxFrame = gray.copy()
            faces = faceClassif.detectMultiScale(gray,1.3,5)
            for(x,y,w,h) in faces:
                rostro = auxFrame[y:y+h,x:x+w]
                rostro = cv2.resize(rostro,(150,150),interpolation= cv2.INTER_CUBIC)
                result = face_recognizer.predict(rostro)
                cv2.putText(frame,'{}'.format(result),(x,y-5),1,1.3,(255,255,0),1,cv2.LINE_AA)
                #MODELO FisherFace
                if result[1] < 70:
                    cv2.putText(frame,'{}'.format(imagePaths[result[0]]),(x,y-25),2,1.1,(255,255,0),1,cv2.LINE_AA)
                    cv2.rectangle(frame, (x,y),(x+w,y+h),(0,255,0),2)
                    lista = json.dumps(imagePaths[result[0]])
                    return lista
                    break
                # lista = json.dumps(imagePaths[result[0]])
                    """if not lista:
                        lista = json.dumps(imagePaths[result[0]])
                        print(lista)
                        break
                    else:
                        print(imagePaths[result[0]])
                        break"""
                else:
                    cv2.putText(frame,'Desconocido',(x,y-20),2,0.8,(0,0,255),1,cv2.LINE_AA)
                    cv2.rectangle(frame, (x,y),(x+w,y+h),(0,255,0),2)
                    lista = json.dumps(imagePaths[result[0]])
                    print("Desconocido")
                    return "Desconocido"
                    """ if not lista:
                        lista = json.dumps(imagePaths[result[0]])
                        print(lista)
                        break
                    else:
                        print(imagePaths[result[0]])
                        break """
            #cv2.imshow('frame',frame)
            
            k = cv2.waitKey(1)
            if k == 27:
                break
        if(lista != "NO ENTRA"):
            print(lista)
            return lista
        cap.release()
        cv2.destroyAllWindows()

@app.errorhandler(404)
def not_found(error):
    return "Not Found."

@app.route('/',methods=['GET'])
def Index():
    return jsonify({'Mensaje':'Bienvenido'})

@app.route('/reconocimiento', methods=['GET'])
def reconocimientofacial():
   # datajson = request.get_json(force=True)
  #  reco_user_ruta = datajson['reco_user_ruta']
    #obj = reconocimiento("Imagenes y Videos de Prueba/Jorge.mp4")
    obj = reconocimiento("reco_user_ruta")
    return jsonify({'Mensaje': obj.reconocimientousuario()})

if __name__ == '__main__':
    app.run(port = 5000, debug = False)

    


