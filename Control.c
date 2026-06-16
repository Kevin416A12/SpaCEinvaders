#include <Keyboard.h>

const int buttonP = 0; // GP0 -> P
const int buttonRL = 1; // GP1 -> R / L doble disparo

bool lastPState = HIGH;
bool lastRLState = HIGH;

unsigned long lastRLPressTime = 0;
bool RLWaiting = false;
const unsigned long RLDoubleTime = 500; // 0.5 segundos

void ledCB(bool numlock, bool capslock, bool scrolllock, bool compose, bool kana, void *cbData) {
  (void) numlock; (void) scrolllock; (void) compose; (void) kana; (void) cbData;
  digitalWrite(LED_BUILTIN, capslock ? HIGH : LOW);
}

void setup() {
  Serial.begin(115200);

  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);

  pinMode(buttonP, INPUT_PULLUP);
  pinMode(buttonRL, INPUT_PULLUP);

  Keyboard.onLED(ledCB);
  Keyboard.begin();

  Serial.println("Teclado USB listo");
  Serial.println("GP0 -> P");
  Serial.println("GP1 -> R/L doble pulsacion");
}

void loop() {
  bool pState = digitalRead(buttonP);
  bool rlState = digitalRead(buttonRL);
  unsigned long currentMillis = millis();

  // Botón P simple
  if (lastPState == HIGH && pState == LOW) {
    Serial.println("Boton GP0 presionado -> P");
    Keyboard.press('p');
    delay(50);
    Keyboard.release('p');
  }
  lastPState = pState;

  // Botón RL con doble pulsación
  if (lastRLState == HIGH && rlState == LOW) {
    if (RLWaiting && (currentMillis - lastRLPressTime <= RLDoubleTime)) {
      // Segunda pulsación dentro de 0.5s -> enviar L en lugar de R
      Serial.println("Boton GP1 doble pulsacion -> L");
      Keyboard.press('l');
      delay(50);
      Keyboard.release('l');
      RLWaiting = false; // reiniciar estado
    } else {
      // Primera pulsación -> esperar posible segunda
      RLWaiting = true;
      lastRLPressTime = currentMillis;
    }
  }

  // Verificar tiempo de espera para disparar R si no llega segunda pulsación
  if (RLWaiting && (currentMillis - lastRLPressTime > RLDoubleTime)) {
    Serial.println("Boton GP1 unica pulsacion -> R");
    Keyboard.press('r');
    delay(50);
    Keyboard.release('r');
    RLWaiting = false;
  }

  lastRLState = rlState;
  delay(10);
}