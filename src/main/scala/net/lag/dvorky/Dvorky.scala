package net.lag.dvorky

import java.util.{List => JList}
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent
import android.text.InputType
import android.content.Context
import android.util.AttributeSet
import android.inputmethodservice.{InputMethodService, Keyboard, KeyboardView}
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo


class Dvorky extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val tv = new TextView(this)
    tv.setText("Hello, cruel world!")
    setContentView(tv)
  }
}

class DvorkyKeyboard extends InputMethodService with KeyboardView.OnKeyboardActionListener {
  var keyboard1: Keyboard = null
  var keyboard2: Keyboard = null
  var view: KeyboardView = null

  // capslock:
  var lastShiftTime: Long = 0
  var capsLock = false

  override def onInitializeInterface() {
    keyboard1 = new Keyboard(this, R.xml.keyboard1)
    keyboard2 = new Keyboard(this, R.xml.keyboard2)
  }

  override def onCreateInputView() = {
    view = getLayoutInflater().inflate(R.layout.input, null).asInstanceOf[KeyboardView]
    view.setKeyboard(keyboard1)
    view.setOnKeyboardActionListener(this)
    view
  }

  override def onStartInput(attribute: EditorInfo, restarting: Boolean) {
    super.onStartInput(attribute, restarting)
  }

  override def onStartInputView(attribute: EditorInfo, restarting: Boolean) {
    super.onStartInputView(attribute, restarting)
    view.setKeyboard(keyboard1)
    view.closing()
  }


  // OnKeyboardActionListener

  def onKey(primaryCode: Int, keyCodes: Array[Int]) {
    println("keycode:" + primaryCode + "/" + keyCodes.toList.toString)
    primaryCode match {
      case Keyboard.KEYCODE_DELETE =>
        sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL)
      case Keyboard.KEYCODE_SHIFT if view ne null =>
        if (view.getKeyboard() == keyboard1) {
          checkToggleCapsLock()
          view.setShifted(capsLock || !view.isShifted())
        }
      case Keyboard.KEYCODE_CANCEL =>
        requestHideSelf(0)
        view.closing()
      case Keyboard.KEYCODE_MODE_CHANGE if view ne null =>
        if (view.getKeyboard() == keyboard1) {
          view.setKeyboard(keyboard2)
        } else {
          view.setKeyboard(keyboard1)
        }
        keyboard1.setShifted(false)
        keyboard2.setShifted(false)
      case _ =>
        handleKey(primaryCode, keyCodes)
        updateShiftKeyState()
    }
  }

  private def handleKey(primaryCode: Int, keyCodes: Array[Int]) {
    val translatedCode = if (isInputViewShown() && view.isShifted()) {
      Character.toUpperCase(primaryCode)
    } else {
      primaryCode
    }
    getCurrentInputConnection().commitText(String.valueOf(translatedCode.toChar), 1)
  }

  private def checkToggleCapsLock() {
    val now = System.currentTimeMillis();
    if (lastShiftTime + 800 > now || capsLock) {
      capsLock = !capsLock
      lastShiftTime = 0
    } else {
      lastShiftTime = now;
    }
  }

  private def updateShiftKeyState() {
    val ei = getCurrentInputEditorInfo()
    if ((ei ne null) && (view ne null) && (view.getKeyboard() == keyboard1)) {
      val caps = (ei.inputType != InputType.TYPE_NULL && getCurrentInputConnection().getCursorCapsMode(ei.inputType) != 0)
      view.setShifted(capsLock || caps)
    }
  }

  def onPress(primaryCode: Int) { }
  def onRelease(primaryCode: Int) { }
  def onText(text: CharSequence) { }
  def swipeRight() { }
  def swipeLeft() { }
  def swipeDown() { }
  def swipeUp() { }
}
