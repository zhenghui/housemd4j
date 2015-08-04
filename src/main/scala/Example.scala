/**
 * user: zhenghui on 2015/8/4.
 * date: 2015/8/4
 * time :22:16
 * email: zhenghui.cjb@taobao.com
 */
import com.github.zhongl.yascli._

object Example extends Command(name = "example", description = "a example of single command") with Application {
  private val flag0       = flag("-f" :: "--flag" :: Nil, "enable flag")
  private val singleValue = option[String]("--single-value" :: Nil, "set single value", "value")
  private val param       = parameter[String]("param", "set param")

  override def run() {
    if (flag0()) println("enable flag0.")
    println(singleValue())
    println(param())
  }
}
