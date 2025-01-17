package ld48.models

import indigo._
import ld48.HelloIndigo

case class Platform(y: Double, blockList: Seq[Option[Block]]) {
  def update(speed: Double, t: Seconds, halted: Boolean): Platform =
    if (halted) this else this.copy(y = y - (speed + 30) * t.toDouble * 1.5)

  def render =
    blockList.zipWithIndex.collect { case (Some(block), i) =>
      block.render.moveTo(Point(i * 32, y.toInt))
    }
}

object Platform {
  def getRandomBlock(r: scala.util.Random): Option[Block] =
    if (r.nextFloat() > 0.2) Option(Block()) else None

  def addPlatform(y: Double): Platform = {
    val r = new scala.util.Random()
    val platforms =
      Seq.fill(11)(Option(Block())) // default platform is all blocks
    val holeIndex: Integer = r.between(1, 11)
    val updatedPlatforms   = platforms.updated(holeIndex, None)
    Platform(
      y, // add platforms under the map in case player gets a lucky fall through
      updatedPlatforms
    )
  }

  def initial =
    (for (i <- 300 to 800 if i % 100 == 0)
      yield i.toDouble).map(addPlatform)

}

case class Block(btype: Int = 0) {
  def render = Graphic(
    width = 32,
    height = 32,
    Material.Bitmap(HelloIndigo.blockAssetName)
  )
    .withCrop(Rectangle.apply(0, 0, 32, 8))
    .withRef(0, 0)

}
object Block {
  def hitbox(y: Double, i: Int) = {
    val ul = Vector2(i * 32, y)
    val lr = ul + Vector2(32, 6)
    Rectangle.fromTwoPoints(ul.toPoint, lr.toPoint)
  }
}
