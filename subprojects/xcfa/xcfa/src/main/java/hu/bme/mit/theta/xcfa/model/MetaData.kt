/*
 *  Copyright 2025 Budapest University of Technology and Economics
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package hu.bme.mit.theta.xcfa.model

abstract class MetaData {

  abstract fun combine(other: MetaData): MetaData

  abstract fun isSubstantial(): Boolean
}

object EmptyMetaData : MetaData() {
  // TODO why is this necessary? {@see GsonTest.kt}
  private val hash = 123123

  override fun combine(other: MetaData): MetaData {
    return other
  }

  override fun isSubstantial(): Boolean {
    return false
  }

  override fun equals(other: Any?): Boolean = other is EmptyMetaData

  override fun hashCode(): Int = hash
}
