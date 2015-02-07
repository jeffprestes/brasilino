int MA = 4;
int MB = 5;
int MC= 6;
int MD = 7;
int i = 0; 

// the setup routine runs once when you press reset:
void setup() {                
  // initialize the digital pin as an output.
  pinMode(MA, OUTPUT);
  pinMode(MB, OUTPUT);
  pinMode(MC, OUTPUT);
  pinMode(MD, OUTPUT);
  Serial.begin(9600);
}

// the loop routine runs over and over again forever:
void loop() {
  

  if (i<2)   { 
    digitalWrite(MA, HIGH);    // turn the LED off by making the voltage LOW
    delay(3000);
    digitalWrite(MA, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MB, HIGH);    // turn the LED off by making the voltage LOW
    delay(3000);
    digitalWrite(MB, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MC, HIGH);    // turn the LED off by making the voltage LOW
    delay(600);
    digitalWrite(MC, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MD, HIGH);    // turn the LED off by making the voltage LOW
    delay(600);
    digitalWrite(MD, LOW);// turn the LED on (HIGH is the voltage level)
    Serial.println("Moveu");
    i++;
  }
}
