package ui
import ingest.Functions.Item
import ingest.SearchConsole.{ASYN, searchMultiple}
import ingest.{Functions, SearchConsole}
import retrieval.UseCases

import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.event.{ButtonClicked, EditDone}

//TODO: an input field, a button, and a responsive content showing result
class UI extends MainFrame {
  title = "CSYE7200_FINAL_PROJECT"
  preferredSize = new Dimension(1000, 1000)
  contents = new Contents
}

object inc {
  var s = 0
  def ic()={
    s +=1
  }
}

class Contents extends BoxPanel(Orientation.Vertical) {
  val instructionLabel = new Label("Please type in some keywords you want to search, separated by comma")
  private def restrictHeight(s: Component) {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  val searchField = new TextField { columns = 32 }
  //contents += searchField
  val searchButton= new Button("Search")
  //contents += Button("Close") { sys.exit(0) }
  val searchLine = new BoxPanel(Orientation.Horizontal) {
    contents += searchField
    contents += Swing.HStrut(20)
    contents += searchButton
  }

  val resultField = new TextArea {
    rows = 10
    lineWrap = true
    wordWrap = true
    editable = false
  }

  restrictHeight(searchLine)

  //contents //= new BoxPanel(Orientation.Vertical){
  contents += instructionLabel
  contents += searchLine
  contents += Swing.VStrut(10)
  val showField = new ScrollPane(resultField)
  contents += showField
  border = Swing.EmptyBorder(10, 10, 10, 10)
  //}
  listenTo(searchField)
  listenTo(searchButton)
  //listenTo(resultField)
  reactions += {
    //case EditDone(`searchField`) => searchNow()
    case ButtonClicked(`searchButton`) => searchNow()
  }

  val timer = new javax.swing.Timer(100, Swing.ActionListener(e =>
  {
    //resultField.repaint()
    // showField.repaint()
    inc.ic()
    resultField.text=inc.s.toString
    showField.repaint()
  }))

  val timer2 = new javax.swing.Timer(10000, Swing.ActionListener(e =>
  {
    //resultField.repaint()
    // showField.repaint()
    val pattern = searchField.text.toLowerCase
    val res = doProgram1(pattern)
    val res2 = doProgram2(pattern)
    //showField.repaint()
  }))

  def searchNow(): Unit ={
    //timer.start()
    //timer2.start()
    val pattern = searchField.text.toLowerCase
    val res = doProgram1(pattern)
    val res2 = doProgram2(pattern)
    //val pattern = searchField.text.toLowerCase
    //val res = doProgram(pattern)
    //resultField.text = res
    //println(pattern)
    //val pattern = searchField.text.toLowerCase
    //val res = doProgram1(pattern)
    //val res2 = doProgram2(pattern)
    //resultField.text = "123"
    //timer.stop()
  }

  def doProgram(s : String) : String={
    SearchConsole.SEARCH_KEYWORDS = s
    val buf = scala.collection.mutable.ListBuffer.empty[Item]
    SearchConsole.RESPONSE_TIME_MILLI = 1000
    SearchConsole.ASYN = true
    searchAllCategoriesLinear(buf)
    getResult(buf)
  }

  def doProgram1(s : String) : String={
    SearchConsole.SEARCH_KEYWORDS = s
    val buf = scala.collection.mutable.ListBuffer.empty[Item]
    SearchConsole.RESPONSE_TIME_MILLI = 1000
    SearchConsole.ASYN = true
    searchAllCategoriesLinear1(buf)
    getResult(buf)
  }

  def doProgram2(s : String) : String={
    SearchConsole.SEARCH_KEYWORDS = s
    val buf = scala.collection.mutable.ListBuffer.empty[Item]
    SearchConsole.RESPONSE_TIME_MILLI = 1000
    SearchConsole.ASYN = true
    searchAllCategoriesLinear2(buf)
    getResult(buf)
  }

  def getResult(buf: ListBuffer[Item]):String={
    val colorsLower = UseCases.getColors(buf)

    val brandsUpper = UseCases.getBrands(buf)

    val pricesDouble = UseCases.getPrices(buf)
    //TODO: When the list grows too big, we might need Map-Reduce to process it; generating new items and added to buf while reading buf using RDD
    println(Functions.sortResultAscending(colorsLower))
    println(Functions.sortResultAscending(brandsUpper))
    println(pricesDouble)
    println(buf.toList.size)
    val sb = new StringBuilder
    sb.append(Functions.sortResultAscending(colorsLower))
    sb.append('\n')
    sb.append(Functions.sortResultAscending(brandsUpper))
    sb.append('\n')
    sb.append(pricesDouble)
    sb.append('\n')
    sb.toString()
  }

  def searchAllCategoriesLinear(buf: ListBuffer[Item], flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Fashion",flag)
    searchMultiple(buf,6,10,"Fashion",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"FashionBaby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"FashionBaby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"FashionBoys",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"FashionBoys",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"FashionGirls",flag)
    searchMultiple(buf,6,10,"FashionGirls",flag)
    searchMultiple(buf,1,5,"FashionMen",flag)
    searchMultiple(buf,6,10,"FashionMen",flag)
    searchMultiple(buf,1,5,"FashionWomen",flag)
    searchMultiple(buf,6,10,"FashionWomen",flag)
    searchMultiple(buf,1,5,"Appliances",flag)
    searchMultiple(buf,6,10,"Appliances",flag)
    searchMultiple(buf,1,5,"ArtsAndCrafts",flag)
    searchMultiple(buf,6,10,"ArtsAndCrafts",flag)
    searchMultiple(buf,1,5,"Automotive",flag)
    searchMultiple(buf,6,10,"Automotive",flag)
    searchMultiple(buf,1,5,"Baby",flag)
    searchMultiple(buf,6,10,"Baby",flag)
    searchMultiple(buf,1,5,"Beauty",flag)
    searchMultiple(buf,6,10,"Beauty",flag)
    searchMultiple(buf,1,5,"Blended",flag)
    searchMultiple(buf,6,10,"Blended",flag)
    searchMultiple(buf,1,5,"Books",flag)
    searchMultiple(buf,6,10,"Books",flag)
    searchMultiple(buf,1,5,"Collectibles",flag)
    searchMultiple(buf,6,10,"Collectibles",flag)
    searchMultiple(buf,1,5,"Electronics",flag)
    searchMultiple(buf,6,10,"Electronics",flag)
    searchMultiple(buf,1,5,"GiftCards",flag)
    searchMultiple(buf,6,10,"GiftCards",flag)
    searchMultiple(buf,1,5,"Grocery",flag)
    searchMultiple(buf,6,10,"Grocery",flag)
    searchMultiple(buf,1,5,"HealthPersonalCare",flag)
    searchMultiple(buf,6,10,"HealthPersonalCare",flag)
    searchMultiple(buf,1,5,"HomeGarden",flag)
    searchMultiple(buf,6,10,"HomeGarden",flag)
    searchMultiple(buf,1,5,"Industrial",flag)
    searchMultiple(buf,6,10,"Industrial",flag)
    searchMultiple(buf,1,5,"KindleStore",flag)
    searchMultiple(buf,6,10,"KindleStore",flag)
    searchMultiple(buf,1,5,"LawnAndGarden",flag)
    searchMultiple(buf,6,10,"LawnAndGarden",flag)
    searchMultiple(buf,1,5,"Luggage",flag)
    searchMultiple(buf,6,10,"Luggage",flag)
    searchMultiple(buf,1,5,"MP3Downloads",flag)
    searchMultiple(buf,6,10,"MP3Downloads",flag)
    searchMultiple(buf,1,5,"Magazines",flag)
    searchMultiple(buf,6,10,"Magazines",flag)
    searchMultiple(buf,1,5,"Merchants",flag)
    searchMultiple(buf,6,10,"Merchants",flag)
    searchMultiple(buf,1,5,"MobileApps",flag)
    searchMultiple(buf,6,10,"MobileApps",flag)
    searchMultiple(buf,1,5,"Movies",flag)
    searchMultiple(buf,6,10,"Movies",flag)
    searchMultiple(buf,1,5,"Music",flag)
    searchMultiple(buf,6,10,"Music",flag)
    searchMultiple(buf,1,5,"MusicalInstruments",flag)
    searchMultiple(buf,6,10,"MusicalInstruments",flag)
    searchMultiple(buf,1,5,"OfficeProducts",flag)
    searchMultiple(buf,6,10,"OfficeProducts",flag)
    searchMultiple(buf,1,5,"PCHardware",flag)
    searchMultiple(buf,6,10,"PCHardware",flag)
    searchMultiple(buf,1,5,"PetSupplies",flag)
    searchMultiple(buf,6,10,"PetSupplies",flag)
    searchMultiple(buf,1,5,"Software",flag)
    searchMultiple(buf,6,10,"Software",flag)
    searchMultiple(buf,1,5,"SportingGoods",flag)
    searchMultiple(buf,6,10,"SportingGoods",flag)
    searchMultiple(buf,1,5,"Tools",flag)
    searchMultiple(buf,6,10,"Tools",flag)
    searchMultiple(buf,1,5,"Toys",flag)
    searchMultiple(buf,6,10,"Toys",flag)
    searchMultiple(buf,1,5,"UnboxVideo",flag)
    searchMultiple(buf,6,10,"UnboxVideo",flag)
    searchMultiple(buf,1,5,"VideoGames",flag)
    searchMultiple(buf,6,10,"VideoGames",flag)
    searchMultiple(buf,1,5,"Wine",flag)
    searchMultiple(buf,6,10,"Wine",flag)
    searchMultiple(buf,1,5,"Wireless",flag)
    searchMultiple(buf,6,10,"Wireless",flag)
  }

  def searchAllCategoriesLinear1(buf: ListBuffer[Item], flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Fashion",flag)
    println("#####")
    resultField.text= getResult(buf)
    println("#####")
    Thread.sleep(100)
    showField.repaint()
    println("#####")
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Fashion",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"FashionBaby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"FashionBaby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"FashionBoys",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"FashionBoys",flag)
    resultField.text= getResult(buf)
    showField.repaint()
  }

  def searchAllCategoriesLinear2(buf: ListBuffer[Item], flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Automotive",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Automotive",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"Baby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Baby",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"Beauty",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Beauty",flag)
    resultField.text= getResult(buf)
    searchMultiple(buf,1,5,"Blended",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Blended",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,1,5,"Books",flag)
    resultField.text= getResult(buf)
    showField.repaint()
    searchMultiple(buf,6,10,"Books",flag)
  }
}

object GUIProgram{
  def main(args: Array[String]){
    val ui =  new UI
    ui.visible = true
    println("starting program")
  }
}
