int val;
int tempPin =1;
//C:\Users\VICTOR SHININGSTAR\AppData\Local\Temp\arduino_build_366061\temp.ino.hex
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  val = analogRead(tempPin);
  float mv = ( val/1024.0)*5000;
  float cel = mv/10;

  Serial.print("TEMPERATURE = ");
  Serial.print(cel);
   Serial.print("*C");
   Serial.println();
   delay(1000);
}
