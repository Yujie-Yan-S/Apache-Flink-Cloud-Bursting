/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nexmark.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/** A person either creating an auction or making a bid. */
public class Person implements Serializable {

	/** Id of person. */
	public long id; // primary key

	/** Extra person properties. */
	public String name;

	public String emailAddress;

	public String creditCard;

	public String city;

	public String state;

	public Instant dateTime;

	/** Additional arbitrary payload for performance testing. */
	public String extra;

	public Person(
			long id,
			String name,
			String emailAddress,
			String creditCard,
			String city,
			String state,
			Instant dateTime,
			String extra) {
		this.id = id;
		this.name = name;
		this.emailAddress = emailAddress;
		this.creditCard = creditCard;
		this.city = city;
		this.state = state;
		this.dateTime = dateTime;
		this.extra = extra;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", creditCard='" + creditCard + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", dateTime=" + dateTime +
				", extra='" + extra + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Person person = (Person) o;
		return id == person.id
			&& Objects.equals(dateTime, person.dateTime)
			&& Objects.equals(name, person.name)
			&& Objects.equals(emailAddress, person.emailAddress)
			&& Objects.equals(creditCard, person.creditCard)
			&& Objects.equals(city, person.city)
			&& Objects.equals(state, person.state)
			&& Objects.equals(extra, person.extra);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, emailAddress, creditCard, city, state, dateTime, extra);
	}
}
