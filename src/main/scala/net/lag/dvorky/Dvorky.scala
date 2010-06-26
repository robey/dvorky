package net.lag.dvorky

import java.util.{List => JList}
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent

class Dvorky extends Activity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val tv = new TextView(this)
    tv.setText("Hello, cruel world!")
    setContentView(tv)
  }
}


import android.inputmethodservice.{InputMethodService, Keyboard, KeyboardView}
import android.view.inputmethod.EditorInfo

class DvorkyKeyboard extends InputMethodService with KeyboardView.OnKeyboardActionListener {
  var mainKeyboard: Keyboard = null
  var view: KeyboardView = null

  override def onInitializeInterface() {
    mainKeyboard = new Keyboard(this, R.xml.main)
  }

  override def onCreateInputView() = {
    view = getLayoutInflater().inflate(R.layout.input, null).asInstanceOf[KeyboardView]
    view.setKeyboard(mainKeyboard)
    view.setOnKeyboardActionListener(this)
    view
  }
  
  override def onStartInput(attribute: EditorInfo, restarting: Boolean) {
    super.onStartInput(attribute, restarting)
    println("hello!")
  }

  override def onStartInputView(attribute: EditorInfo, restarting: Boolean) {
    super.onStartInputView(attribute, restarting)
    view.setKeyboard(mainKeyboard)
    view.closing()
    println("hello 2!")
  }
  
  
  // OnKeyboardActionListener
  
  def onKey(primaryCode: Int, keyCodes: Array[Int]) { }
  def onText(text: CharSequence) { }
  def updateCandidates() { }
  def setSuggestions(suggestions: JList[String], completions: Boolean) { }
  def handleBackspace() { }
  def handleShift() { }
  def handleCharacter(primaryCode: Int, keyCodes: Array[Int]) { }
  def handleClose() { }
  def checkToggleCapsLock() { }
  def isWordSeparator(code: Int): Boolean = { code == 20 }
  def pickDefaultCandidate() { }
  def pickSuggestionManually(index: Int) { }
  def swipeRight() { }
  def swipeLeft() { }
  def swipeDown() { }
  def swipeUp() { }
  def onPress(primaryCode: Int) { }
  def onRelease(primaryCode: Int) { }
}

import android.content.Context
import android.util.AttributeSet

class DvorkyKeyboardView(context: Context, attrs: AttributeSet) extends KeyboardView(context, attrs) {
  val KEYCODE_OPTIONS = -100;

  override protected def onLongPress(key: Keyboard.Key): Boolean = {
    if (key.codes(0) == Keyboard.KEYCODE_CANCEL) {
      getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null)
      true
    } else {
      super.onLongPress(key)
    }
  }
}
