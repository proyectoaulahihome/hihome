import cv2
import os
import numpy as np

absolutepath = os.path.abspath(__file__)
fileDirectory = os.path.dirname(absolutepath)
parentDirectory = os.path.dirname(fileDirectory)
dataPath = os.path.join(parentDirectory, 'Data')
peopleList = os.listdir(dataPath.replace('\\', '/'))
print('Lista de personas: ', peopleList)

labels = []
#Presentar las imagenes con los rostros que debemos reconocer al computador 
facesData = []
#Pero como sabe el computador de que persona se trata
#Para ello usamos una etiqueta
#Por ejemplo, todas las imagenes #de Jorge tendrán la etiqueta 0, todas las de Juan la etiqueta 1.
#Si fueran más personas a reconocer, tuvieran otra etiqueta diferente y de ese modo el computador
#sabrá de quien se trata
label = 0

for nameDir in peopleList:
    personPath = dataPath + '/' + nameDir
    print ('Leyendo las imágenes')

    for fileName in os.listdir(personPath):
        print('Rostros: ', nameDir + '/' + fileName)
        #almacenar los rostros en las etiquetas que corresponde a cada una
        #añadiendo el valor de label en Labels
        labels.append(label)
        # en facesData añadimos cada una de las imagenes en escala de grises
        #  en imread, especificamos la dirección donde está almanecada la imagen
        # y con 0 estamos realizando la transformación a escala de grisis
        facesData.append(cv2.imread(personPath+'/'+fileName,0))
        image = cv2.imread(personPath+'/'+fileName,0)
        #Mostrar imagen por imagen que se va entrenando con
        #cv2.imshow('image', image)
        #cv2.waitKey(10)
    label = label + 1

#print('labels= ', labels)
##Encontrar el número de 0 presentes
#print('Número de etiquetas 0: ',np.count_nonzero(np.array(labels)==0))
##Encontrar el número de 1 presentes
#print('Número de etiquetas 1: ',np.count_nonzero(np.array(labels)==1))
#----------- MODELO EIGEN
#face_recognizer = cv2.face.EigenFaceRecognizer_create()
#----------- MODELO FISHER
#face_recognizer = cv2.face.FisherFaceRecognizer_create()
#----------- MODELO LBPH
face_recognizer = cv2.face.LBPHFaceRecognizer_create()
# Entrenando el reconocedor de rostros
print("Entrenando...")
#Empezando el entrenamiento
#En los parentesis especificamos el array donde está almacenado todos los rostros
# y luego las etiquetas de cada una de estos que teniamos en labels
# se pone en un np.array porque necesitamos que esos elementos sean numpy array
face_recognizer.train(facesData, np.array(labels))

#Una vez entrenado el reconocedor de rostros es posible almacenarlo
# guardamos el modelo obtenido para luego leer en otro script
# Almacenando el modelo obtenido
#----------- MODELO EIGEN
#face_recognizer.write('modeloEigenFace.xml')
#----------- MODELO FISHER
#face_recognizer.write('modeloFisherFace.xml')
#----------- MODELO LBPH
face_recognizer.write('reconocimiento_Facial/Modelos/modeloLBPHFace.xml')
print("Modelo almacenado.....")
#cv2.destroyAllWindows()