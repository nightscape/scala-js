/*
 * Scala.js (https://www.scala-js.org/)
 *
 * Copyright EPFL.
 *
 * Licensed under Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package org.scalajs.testsuite.javalib.io

import java.io._

import scala.language.implicitConversions

import org.junit.Test
import org.junit.Assert._

class ByteArrayOutputStreamTest {

  @Test def simpleWriteInt(): Unit = {
    val out = new ByteArrayOutputStream()

    for (i <- 0 to 9)
      out.write(i)

    assertArrayEquals(Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9), out.toByteArray)
  }

  @Test def simpleWriteByteArray(): Unit = {
    val out = new ByteArrayOutputStream()
    val arr = Array[Byte](0, 1, 2, 3, 4, 5)

    out.write(arr, 1, 4)
    out.write(arr)

    assertArrayEquals(Array[Byte](1, 2, 3, 4, 0, 1, 2, 3, 4, 5), out.toByteArray)
  }

  @Test def writeByteArrayWithBufferResize(): Unit = {
    val out = new ByteArrayOutputStream(16)
    val arr = Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    out.write(arr)
    out.write(arr)

    assertArrayEquals(arr ++ arr, out.toByteArray)
  }

  @Test def toStringWithUTF8(): Unit = {
    val buf = Array[Byte](72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100,
      46, -29, -127, -109, -29, -126, -109, -29, -127, -85, -29, -127, -95,
      -29, -127, -81, -26, -105, -91, -26, -100, -84, -24, -86, -98, -29,
      -126, -110, -24, -86, -83, -29, -126, -127, -29, -127, -66, -29, -127,
      -103, -29, -127, -117, -29, -128, -126)

    val out = new ByteArrayOutputStream()
    out.write(buf)

    assertEquals("Hello World.こんにちは日本語を読めますか。", out.toString)
  }

  @Test def reset(): Unit = {
    val out = new ByteArrayOutputStream()
    for (i <- 0 to 9) out.write(i)
    out.reset()
    for (i <- 0 to 9) out.write(i)

    assertArrayEquals(Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9), out.toByteArray)
  }

  @Test def bufField(): Unit = {
    class ByteArrayOutputStreamWithBufAccess extends ByteArrayOutputStream {
      def getBuf(): Array[Byte] = buf
      def setBuf(b: Array[Byte]): Unit = buf = b
    }

    val os = new ByteArrayOutputStreamWithBufAccess
    os.write(5.toInt)
    os.flush()
    assertEquals(5.toByte, os.getBuf()(0))

    val newBuf = Array(10.toByte)
    os.setBuf(newBuf)
    assertSame(newBuf, os.getBuf())
    val output = os.toByteArray()
    assertArrayEquals(newBuf, output)
    assertNotSame(newBuf, output)
  }

  @Test def countField(): Unit = {
    class ByteArrayOutputStreamWithCountAccess extends ByteArrayOutputStream {
      def getCount(): Int = count
      def setCount(c: Int): Unit = count = c
    }

    val os = new ByteArrayOutputStreamWithCountAccess
    os.write(Array[Byte](5, 7, 10, 15, 25, -4))
    os.flush()
    assertEquals(6, os.getCount())
    assertArrayEquals(Array[Byte](5, 7, 10, 15, 25, -4), os.toByteArray())

    os.setCount(3)
    assertEquals(3, os.getCount())
    assertEquals(3, os.size())
    assertArrayEquals(Array[Byte](5, 7, 10), os.toByteArray())
  }

}
