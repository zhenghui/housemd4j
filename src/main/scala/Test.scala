

/**
 * user: zhenghui on 2015/8/4.
 * date: 2015/8/4
 * time :22:32
 * email: zhenghui.cjb@taobao.com
 */
object Test {

  private val parameters = "a" :: "b" :: Nil


  def main(args: Array[String]): Unit = {
    val list = List("c","d")
    Test.read(list)
  }

  def query(index: Int, list: List[String]): List[String] = parameters(index) match {
    case p => println(p);list.tail
  }

  def read(list:List[String])(implicit index: Int = 0): Unit ={
    list match {
      case haed::tail => read(query(index,list))(index + 1)
      case _ =>
    }
  }


}
