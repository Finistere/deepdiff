/*
 * Copyright 2020 Criteo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.criteo.hadoop.deepdiff.type_support.unsafe

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.UnsafeRow

private[unsafe] abstract class UnsafeRowComparator extends UnsafeComparator[InternalRow] {
  import UnsafeComparison._
  @inline def compare(x: InternalRow, y: InternalRow): UnsafeComparison = {
    if (x.isInstanceOf[UnsafeRow] && y.isInstanceOf[UnsafeRow]) {
      compareUnsafe(x.asInstanceOf[UnsafeRow], y.asInstanceOf[UnsafeRow])
    } else {
      Unknown
    }
  }

  @inline protected def compareUnsafe(x: UnsafeRow, y: UnsafeRow): UnsafeComparison
}
