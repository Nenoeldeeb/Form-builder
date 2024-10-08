package com.dsc.form_builder

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class FormStateTest {
	internal data class FormTestDataClass(
		val email: String,
		val hobbies: List<String>,
		val gender: String,
		val age: String,
		val active: Boolean,
		private val date: Int = 0
	)
	
	@Nested
	inner class DescribingFormState {
		
		private val formState = FormState(
			listOf(
				TextFieldState(name = "email"),
				SelectState(name = "hobbies"),
				ChoiceState(name = "gender"),
				TextFieldState(
					name = "age",
					initial = "34"
				),
				SwitchState(name = "active")
			)
		)
		
		private val emailState = formState.getState<TextFieldState>("email")
		private val hobbyState = formState.getState<SelectState>("hobbies")
		private val genderState = formState.getState<ChoiceState>("gender")
		private val ageState = formState.getState<TextFieldState>("age")
		private val statusState = formState.getState<SwitchState>("active")
		
		@Test
		fun `state should be reset to initial values`() {
			// Given a form state with values changed
			emailState.change("buider@gmail.com")
			hobbyState.select("Running")
			genderState.change("male")
			ageState.change("56")
			statusState.toggle()
			
			// When the form.reset is requested
			formState.reset()
			
			// Then all values are reset to the original state
			assert(emailState.value == "" && !emailState.hasError)
			assert(hobbyState.value == mutableListOf<String>() && !hobbyState.hasError)
			assert(genderState.value == "" && !genderState.hasError)
			assert(ageState.value == "34" && !ageState.hasError)
			assert(!statusState.value && !statusState.hasError)
		}
		
		@Test
		fun `getData gets the form's data correctly`() {
			ageState.change("16")
			emailState.change("testget@form.com")
			hobbyState.select("Running")
			hobbyState.select("Reading")
			genderState.change("male")
			statusState.toggle()
			
			val data = formState.getData(FormTestDataClass::class)
			
			assert(data.email == emailState.value)
			assert(data.hobbies == hobbyState.value)
			assert(data.gender == genderState.value)
			assert(data.age == ageState.value)
		}
		
		@Test
		fun `setData should set the correct values`() {
			val data = FormTestDataClass(
				email = "buider@gmail.com",
				hobbies = listOf(
					"Running",
					"Reading"
				),
				gender = "male",
				age = "56",
				active = true,
				date = 12
			)
			formState.setData(data)
			
			val data2 = formState.getData(FormTestDataClass::class)
			assert(data2.email == data.email)
			assert(data2.hobbies == data.hobbies)
			assert(data2.gender == data.gender)
			assert(data2.age == data.age)
			assert(data2.active == data.active)
		}
	}
}
