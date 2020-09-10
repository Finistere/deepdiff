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

package com.criteo.hadoop.deepdiff.raw_part

import com.criteo.hadoop.deepdiff.LeftOnlyExample

/** Mutable structure used to build a [[LeftOnlyRawDiffsPart]].
  *
  * Methods accept the record, and not the field itself as this allows to easily factorize
  * code. Whatever the type of the field, the type of the record doesn't change.
  *
  * @param maxDiffExamples Maximum number of examples to keep.
  * @param rawDiffExampleBuilder Used to generate an example from both records. Called only the
  *                              maximum number of examples is not reached yet.
  * @tparam K key
  * @tparam R record
  * @tparam Kx key example
  * @tparam Fx field example
  */
private[deepdiff] final class LeftOnlyRawDiffsPartBuilder[K, R, Kx, Fx](
    maxDiffExamples: Int,
    rawDiffExampleBuilder: RawDiffExampleBuilder[K, R, Kx, Fx]
) extends Serializable {
  import KindOfDiff._

  private var leftOnlyExamples: List[(Kx, LeftOnlyExample[Fx])] = Nil
  private var nullCount: Long = 0
  private var leftOnlyCount: Long = 0

  @inline def nullValue(): KindOfDiff = {
    nullCount += 1
    IDENTICAL
  }
  @inline def leftOnly(key: K, left: R): KindOfDiff = {
    if (leftOnlyCount < maxDiffExamples) leftOnlyExamples ::= rawDiffExampleBuilder.buildLeftOnly(key, left)
    leftOnlyCount += 1
    LEFT_ONLY
  }

  def result(): LeftOnlyRawDiffsPart[Kx, Fx] =
    LeftOnlyRawDiffsPart[Kx, Fx](
      nulls = nullCount,
      leftOnly = RawDiffPart(leftOnlyCount, leftOnlyExamples)
    )
}
