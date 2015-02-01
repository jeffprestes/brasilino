// Brasilino.ino

int incomingByte = 0;
String cmd;
char c;
char matriz[2];

//Pinos Motores
int MA = 4;
int MB = 5;
int MC= 6;
int MD = 7;

void setup() {
	Serial.begin(9600);

    // initialize the digital pin as an output.
    pinMode(MA, OUTPUT);
    pinMode(MB, OUTPUT);
    pinMode(MC, OUTPUT);
    pinMode(MD, OUTPUT);
}

void loop() {

    if (Serial.available() > 0) {
        cmd = readData();

        Serial.print("[" + cmd + "]");
        //A R E D P F

        //A = Avancar
        if (cmd == "A")    {
            frente();
        }

        //R = Retroceder
        if (cmd == "R")     {
            voltar();
        }

        //P = Parar
        if (cmd == "P")     {
            parar();
        }

        //F = Frear
        if (cmd == "F")     {
            frear();
        }

        if (cmd == "D")     {
            direita();
        }

        if (cmd == "E")     {
            esquerda();
        }
    }

}


void frear()       {
    digitalWrite(MA, LOW);    // turn the LED off by making the voltage LOW
    delay(500);
    digitalWrite(MB, HIGH);    // turn the LED off by making the voltage LOW
    delay(500);
    digitalWrite(MB, LOW);    // turn the LED off by making the voltage LOW

}

//Para todos os motores
void parar()    {
    digitalWrite(MA, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MB, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MC, LOW);// turn the LED on (HIGH is the voltage level)
    digitalWrite(MD, LOW);// turn the LED on (HIGH is the voltage level)
}

//Liga sentido frente
void frente()   {
    digitalWrite(MA, HIGH);    // turn the LED off by making the voltage LOW
    delay(600);
    digitalWrite(MA, LOW);
            
}

//Liga sentido frente
void voltar()   {
    digitalWrite(MB, HIGH);    // turn the LED off by making the voltage LOW
    delay(600);
    digitalWrite(MB, LOW);
            
}

//Liga direita
void direita()   {
    digitalWrite(MD, HIGH);    // turn the LED off by making the voltage LOW
    delay(300);
    digitalWrite(MD, LOW);    // turn the LED off by making the voltage LOW
}


//Liga esquerda
void esquerda()   {
    digitalWrite(MC, HIGH);    // turn the LED off by making the voltage LOW
    delay(300);
    digitalWrite(MC, LOW);    // turn the LED off by making the voltage LOW      
}


//Le as informacoes enviadas pela Serial
String readData()      {

    String retorno;

    do  {
        // read the incoming byte:
        c = Serial.read();
        matriz[incomingByte] = c;
        incomingByte++;
        delay(2);

        // statement block
    } while (Serial.available());

    matriz[incomingByte-1] = '\0';
    retorno = matriz;
    char matriz[2] = {0};
    incomingByte = 0;

    return retorno;

}

