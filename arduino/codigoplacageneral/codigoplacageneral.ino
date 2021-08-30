#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>


#include <Adafruit_MLX90614.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

SoftwareSerial ESPSerial (9,8); // PUERTOS RX Y TX

#define         MQ1                       (0)     //define la entrada analogica para el sensor
#define         RL_VALOR             (5)     //define el valor de la resistencia mde carga en kilo ohms
#define         RAL       (9.83)  // resistencia del sensor en el aire limpio / RO, que se deriva de la                                             tabla de la hoja de datos
#define         GAS_LP                      (0)

// variable json
String jsondata;
String jsondataalertcorporal;
String jsondataalertgas;

// variables mlx90614
int puertoentradaA1;
int puertoentradaA2;
int datodegas;
int datotemp;
int buzzer=2;

String Note= "Sin nota";
bool notification = false;
Adafruit_MLX90614 mlx = Adafruit_MLX90614();
// fin variables mlx90614

String inputstring = "";                                                        //Cadena recibida desde el PC
float           LPCurve[3]  =  {2.3, 0.21, -0.47};
float           Ro           =  10;

// variables sensor infrarrojo
int PinobsSensor = 4;
int valor = 0;
bool band = false;

int hayObstaculo;
// fin variables sensor infrarrojo


void setup() {
  Serial.begin(115200);   //Inicializa Serial a 9600 baudios
ESPSerial.begin(115200);
  Serial.println("Iniciando ...");
  //------------- Sensor de Gas-----------------
  //configuracion del sensor
  Serial.print("Calibrando...\n");
  // mlx
  mlx.begin();
  // fin mlx
  Ro = Calibracion(MQ1);                        //Calibrando el sensor. Por favor de asegurarse que el sensor se encuentre en una zona de aire limpio mientras se calibra
  Serial.print("Calibracion finalizada...\n");
  Serial.print("Ro=");
  Serial.print(Ro);
  Serial.print("kohm");
  Serial.print("\n");

  // mlx
  pinMode(puertoentradaA1, OUTPUT);
  pinMode(puertoentradaA2, OUTPUT);
  pinMode(buzzer,OUTPUT);

  // sensor obstaculo infrarrojo
  pinMode(PinobsSensor, INPUT);
 // pinMode(infrarrojo, INPUT);
}

void loop() {

  valor = digitalRead(PinobsSensor);

  if(valor == LOW){
    band = true;
    Serial.println("Obstaculo detectado");
      
  }else {
    band = false;
  }

  validatemperaturalcorporal();
  //Muestra de los valores en pantalla.
  validadgas();
 // infrarrojo();
  getdata();

  delay(1000);
}

void validadgas()
{
   // datodegas = (porcentaje_gas(lecturaMQ(MQ1) / Ro, GAS_LP)); 
   datodegas = analogRead(A0);
   if(datodegas>200)
   {
    Note = "Alerta de Gas, sobre pasa el limite establecido el valor capturado es de:";
    Note = Note + datodegas;
    notification = true;
   // getdataalertgas();
    prenderBuzeer();
   }
   else {
    apagarBuzzer();
   }                
}

void validatemperaturalcorporal(){
  // temp normal de una persona 37grados C o 97grados F
    mlx90614();
    if(datotemp > 38){
      prenderBuzeer();
      Note = "Alerta de Temperatura del objeto (corporal) sobre pasa el limite establecido el valor capturado es de:";
      Note = Note + datotemp;
      notification = true;
     // getdataalerttempcorporal();
    } else {
      apagarBuzzer();
    }
}

void prenderBuzeer()
{
  digitalWrite(buzzer,HIGH);
}
void apagarBuzzer()
{
   digitalWrite(buzzer,LOW);
}


//Funciones de lectura
float calc_res(int raw_adc)
{
  return ( ((float)RL_VALOR * (1023 - raw_adc) / raw_adc));
}

float Calibracion(float mq_pin) {
  int i;
  float val = 0;
  for (i = 0; i < 50; i++) {                                                                         //tomar múltiples muestras
    val += calc_res(analogRead(mq_pin));
    delay(500);
  }
  val = val / 50;                                                                                       //calcular el valor medio
  val = val / RAL;
  return val;
}

float lecturaMQ(int mq_pin) {
  int i;
  float rs = 0;
  for (i = 0; i < 5; i++) {
    rs += calc_res(analogRead(mq_pin));
    delay(50);
  }
  rs = rs / 5;
  return rs;
}

int porcentaje_gas(float rs_ro_ratio, int gas_id) {
  if ( gas_id == GAS_LP ) {
    return porcentaje_gas(rs_ro_ratio, LPCurve);
  }
  return 0;
}

int porcentaje_gas(float rs_ro_ratio, float *pcurve) {
  return (pow(10, (((log(rs_ro_ratio) - pcurve[1]) / pcurve[2]) + pcurve[0])));
}

void mlx90614() {
  puertoentradaA1 = analogRead(A1);
  puertoentradaA2 = analogRead(A2);
  datotemp =mlx.readObjectTempC();
 // Serial.print(datotemp);
}

void getdata(){
StaticJsonDocument<200> jsonBuffer;
  JsonObject root = jsonBuffer.createNestedObject();
  root["ID"] = "1";
  root["MQGAS"] = datodegas;
  root["MLX"] = datotemp;
  root["MQHUMO"] = 0;
  root["band"] = band;
  serializeJson(root, jsondata);
  ESPSerial.println(jsondata);
  Serial.println(jsondata);
  jsondata = "";
}

void getdataalerttempcorporal(){
  StaticJsonDocument<200> jsonBuffer;
  JsonObject root2 = jsonBuffer.createNestedObject();
  root2["ID"] = "4";
  root2["Note"] = "Alerta de Temperatura del objeto (corporal), sobre pasa el limite establecido";
  root2["MLX"] = datotemp;
  root2["band"] = band;
  serializeJson(root2, jsondataalertcorporal);
  ESPSerial.println(jsondataalertcorporal);
  Serial.println(jsondataalertcorporal);
  jsondataalertcorporal ="";
}

void getdataalertgas(){
  StaticJsonDocument<200> jsonBuffer;
  JsonObject root3 = jsonBuffer.createNestedObject();
  root3["ID"] = "4";
  root3["Note"] = "Alerta de Gas, sobre pasa el limite establecido";
  root3["MQGAS"] = datodegas;
  root3["band"] = band;
  serializeJson(root3, jsondataalertgas);
  ESPSerial.println(jsondataalertgas);
  Serial.println(jsondataalertgas);
  jsondataalertgas ="";
  
}
