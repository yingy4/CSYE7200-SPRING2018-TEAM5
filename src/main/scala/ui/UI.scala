//package ui
//import ingest.Functions.Item
//import ingest.SearchConsole.{ASYN, searchMultiple}
//import ingest.{Functions, SearchConsole}
//import retrieval.UseCases
//
//import scala.collection.mutable.ListBuffer
//import scala.swing._
//import scala.swing.event.{ButtonClicked, EditDone}
//
////TODO: an input field, a button, and a responsive content showing result
//class UI extends MainFrame {
//  title = "CSYE7200_FINAL_PROJECT"
//  preferredSize = new Dimension(1000, 1000)
//  contents = new Contents
//}
//
//class Contents extends BoxPanel(Orientation.Vertical) {
//  val instructionLabel = new Label("Please type in some keywords you want to search, separated by comma")
//  private def restrictHeight(s: Component) {
//    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
//  }
//
//  val searchField = new TextField { columns = 32 }
//  //contents += searchField
//  val searchButton= new Button("Search")
//  //contents += Button("Close") { sys.exit(0) }
//  val searchLine = new BoxPanel(Orientation.Horizontal) {
//    contents += searchField
//    contents += Swing.HStrut(20)
//    contents += searchButton
//  }
//
//  val resultField = new TextArea {
//    rows = 10
//    lineWrap = true
//    wordWrap = true
//    editable = false
//  }
//
//  restrictHeight(searchLine)
//
//  //contents //= new BoxPanel(Orientation.Vertical){
//  contents += instructionLabel
//  contents += searchLine
//  contents += Swing.VStrut(10)
//  contents += new ScrollPane(resultField)
//  border = Swing.EmptyBorder(10, 10, 10, 10)
//  //}
//  listenTo(searchField)
//  listenTo(searchButton)
//  //listenTo(resultField)
//  reactions += {
//    //case EditDone(`searchField`) => searchNow()
//    case ButtonClicked(`searchButton`) => searchNow()
//  }
//
//  def searchNow(): Unit ={
//    val pattern = searchField.text.toLowerCase
//    val res = doProgram(pattern)
//    resultField.text = res
//    //println(pattern)
//  }
//
//  def doProgram(s : String) : String={
//    SearchConsole.SEARCH_KEYWORDS = s
//    val buf = scala.collection.mutable.ListBuffer.empty[Item]
//    //SearchConsole.SEARCH_KEYWORDS = "Trouser"
//    SearchConsole.RESPONSE_TIME_MILLI = 1000
//    SearchConsole.ASYN = true
//    searchAllCategoriesLinear(buf)
//
//
//    //  SearchConsole.searchMultiple(buf, 1,2 )
//    //  SearchConsole.searchWWW(buf)
//
//      getResult(buf)
//  }
//
//  def getResult(buf: ListBuffer[Item]):String={
//    val colorsLower = UseCases.getColors(buf)
//
//    val brandsUpper = UseCases.getBrands(buf)
//
//    val pricesDouble = UseCases.getPrices(buf)
//    //TODO: When the list grows too big, we might need Map-Reduce to process it; generating new items and added to buf while reading buf using RDD
//    println(Functions.sortResultAscending(colorsLower))
//    println(Functions.sortResultAscending(brandsUpper))
//    println(pricesDouble)
//    println(buf.toList.size)
//    val sb = new StringBuilder
//    sb.append(Functions.sortResultAscending(colorsLower))
//    sb.append('\n')
//    sb.append(Functions.sortResultAscending(brandsUpper))
//    sb.append('\n')
//    sb.append(pricesDouble)
//    sb.append('\n')
//    sb.toString()
//  }
//
//  def searchAllCategoriesLinear(buf: ListBuffer[Item], flag: Boolean = ASYN): Unit = {
//    searchMultiple(buf,1,5,"Fashion",flag)
//    resultField.text= getResult(buf)
//    resultField.validate()
//    searchMultiple(buf,6,10,"Fashion",flag)
//    resultField.text= getResult(buf)
//    resultField.validate()
//    searchMultiple(buf,1,5,"FashionBaby",flag)
//    resultField.text= getResult(buf)
//    resultField.validate()
//    searchMultiple(buf,6,10,"FashionBaby",flag)
//    searchMultiple(buf,1,5,"FashionBoys",flag)
//    searchMultiple(buf,6,10,"FashionBoys",flag)
//    searchMultiple(buf,1,5,"FashionGirls",flag)
//    searchMultiple(buf,6,10,"FashionGirls",flag)
//    searchMultiple(buf,1,5,"FashionMen",flag)
//    searchMultiple(buf,6,10,"FashionMen",flag)
//    searchMultiple(buf,1,5,"FashionWomen",flag)
//    searchMultiple(buf,6,10,"FashionWomen",flag)
//    searchMultiple(buf,1,5,"Appliances",flag)
//    searchMultiple(buf,6,10,"Appliances",flag)
//    searchMultiple(buf,1,5,"ArtsAndCrafts",flag)
//    searchMultiple(buf,6,10,"ArtsAndCrafts",flag)
//    searchMultiple(buf,1,5,"Automotive",flag)
//    searchMultiple(buf,6,10,"Automotive",flag)
//    searchMultiple(buf,1,5,"Baby",flag)
//    searchMultiple(buf,6,10,"Baby",flag)
//    searchMultiple(buf,1,5,"Beauty",flag)
//    searchMultiple(buf,6,10,"Beauty",flag)
//    searchMultiple(buf,1,5,"Blended",flag)
//    searchMultiple(buf,6,10,"Blended",flag)
//    searchMultiple(buf,1,5,"Books",flag)
//    searchMultiple(buf,6,10,"Books",flag)
//    searchMultiple(buf,1,5,"Collectibles",flag)
//    searchMultiple(buf,6,10,"Collectibles",flag)
//    searchMultiple(buf,1,5,"Electronics",flag)
//    searchMultiple(buf,6,10,"Electronics",flag)
//    searchMultiple(buf,1,5,"GiftCards",flag)
//    searchMultiple(buf,6,10,"GiftCards",flag)
//    searchMultiple(buf,1,5,"Grocery",flag)
//    searchMultiple(buf,6,10,"Grocery",flag)
//    searchMultiple(buf,1,5,"HealthPersonalCare",flag)
//    searchMultiple(buf,6,10,"HealthPersonalCare",flag)
//    searchMultiple(buf,1,5,"HomeGarden",flag)
//    searchMultiple(buf,6,10,"HomeGarden",flag)
//    searchMultiple(buf,1,5,"Industrial",flag)
//    searchMultiple(buf,6,10,"Industrial",flag)
//    searchMultiple(buf,1,5,"KindleStore",flag)
//    searchMultiple(buf,6,10,"KindleStore",flag)
//    searchMultiple(buf,1,5,"LawnAndGarden",flag)
//    searchMultiple(buf,6,10,"LawnAndGarden",flag)
//    searchMultiple(buf,1,5,"Luggage",flag)
//    searchMultiple(buf,6,10,"Luggage",flag)
//    searchMultiple(buf,1,5,"MP3Downloads",flag)
//    searchMultiple(buf,6,10,"MP3Downloads",flag)
//    searchMultiple(buf,1,5,"Magazines",flag)
//    searchMultiple(buf,6,10,"Magazines",flag)
//    searchMultiple(buf,1,5,"Merchants",flag)
//    searchMultiple(buf,6,10,"Merchants",flag)
//    searchMultiple(buf,1,5,"MobileApps",flag)
//    searchMultiple(buf,6,10,"MobileApps",flag)
//    searchMultiple(buf,1,5,"Movies",flag)
//    searchMultiple(buf,6,10,"Movies",flag)
//    searchMultiple(buf,1,5,"Music",flag)
//    searchMultiple(buf,6,10,"Music",flag)
//    searchMultiple(buf,1,5,"MusicalInstruments",flag)
//    searchMultiple(buf,6,10,"MusicalInstruments",flag)
//    searchMultiple(buf,1,5,"OfficeProducts",flag)
//    searchMultiple(buf,6,10,"OfficeProducts",flag)
//    searchMultiple(buf,1,5,"PCHardware",flag)
//    searchMultiple(buf,6,10,"PCHardware",flag)
//    searchMultiple(buf,1,5,"PetSupplies",flag)
//    searchMultiple(buf,6,10,"PetSupplies",flag)
//    searchMultiple(buf,1,5,"Software",flag)
//    searchMultiple(buf,6,10,"Software",flag)
//    searchMultiple(buf,1,5,"SportingGoods",flag)
//    searchMultiple(buf,6,10,"SportingGoods",flag)
//    searchMultiple(buf,1,5,"Tools",flag)
//    searchMultiple(buf,6,10,"Tools",flag)
//    searchMultiple(buf,1,5,"Toys",flag)
//    searchMultiple(buf,6,10,"Toys",flag)
//    searchMultiple(buf,1,5,"UnboxVideo",flag)
//    searchMultiple(buf,6,10,"UnboxVideo",flag)
//    searchMultiple(buf,1,5,"VideoGames",flag)
//    searchMultiple(buf,6,10,"VideoGames",flag)
//    searchMultiple(buf,1,5,"Wine",flag)
//    searchMultiple(buf,6,10,"Wine",flag)
//    searchMultiple(buf,1,5,"Wireless",flag)
//    searchMultiple(buf,6,10,"Wireless",flag)
//  }
//}
//
//object GUIProgram{
//  def main(args: Array[String]){
//    val ui =  new UI
//    ui.visible = true
//    println("starting program")
//  }
//}
