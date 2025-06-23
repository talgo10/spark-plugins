import org.scalatest.funsuite.AnyFunSuite

class ExampleSparkPluginSuite extends AnyFunSuite {
  test("driverPlugin and executorPlugin return expected instances") {
    val plugin = new com.example.plugin.ExampleSparkPlugin
    assert(plugin.driverPlugin().isInstanceOf[com.example.plugin.ExampleDriverPlugin])
    assert(plugin.executorPlugin().isInstanceOf[com.example.plugin.ExampleExecutorPlugin])
  }
}
