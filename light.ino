
void setup() {
  
  pinMode(2,OUTPUT);
  Serial.begin(9600);

}

void loop() {
  int ldr = analogRead(A2);
   if(ldr<=900){
    digitalWrite(2,HIGH);
    
    Serial.println(ldr);
    }else{
  digitalWrite(2,LOW);
  
  Serial.println(ldr);
  
      }
      
      delay(1000);
    
}
