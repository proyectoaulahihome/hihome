import cv2
import os
import sys
import json
from flask import Flask,jsonify, request

#G:\Mi unidad\2021\Universidad\8vo\Aplicaciones distribuidas\Proyecto_Aula\Data\Jorge
absolutepath = os.path.abspath(__file__)
#print(absolutepath)

fileDirectory = os.path.dirname(absolutepath)
#print(fileDirectory)
#Path of parent directory
parentDirectory = os.path.dirname(fileDirectory)
#print(parentDirectory)
parentDirectory2 = os.path.dirname(parentDirectory)
#print(parentDirectory2)
#Navigate to Strings directory
newPath = os.path.join(fileDirectory, 'Data').replace('\\', '/') 
print(newPath)

#ruta='g:\\Mi unidad\\2021\\Universidad\\8vo\\Aplicaciones distribuidas\\Proyecto_Aula\\Reconocimiento_Facial\\Data'
#print(ruta.replace('\\', '/'))