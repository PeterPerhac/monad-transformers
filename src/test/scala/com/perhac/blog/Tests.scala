package com.perhac.blog

class Tests extends BaseSpec {

  "my program working with Future Options" should passAllChecks(HelloFutureOptions.apply)
  "my program working with OptionTs" should passAllChecks(HelloOptionT.apply)
  "my program working with OptionTs (|>)" should passAllChecks(HelloOptionT2.apply)

  private def passAllChecks(program: MyProgram): Unit = {
    "find loyalty points balance for a user" in {
      program(Seq("1")).futureValue shouldBe "Peter, 33 with a balance of 5000 loyalty points."
    }

    "not find loyalty points balance" when {
      "loyalty programme can't be found" in {
        program(Seq("3")).futureValue shouldBe "User summary for user ID 3 not found"
      }
      "user has not signed up for loyalty programme" in {
        program(Seq("4")).futureValue shouldBe "User summary for user ID 4 not found"
      }
      "non-existent user" in {
        program(Seq("10")).futureValue shouldBe "User summary for user ID 10 not found"
      }
      "the input is invalid" in {
        program(Seq("invalid")).futureValue shouldBe "User summary for user ID invalid not found"
      }
      "no input" in {
        program(Seq()).futureValue shouldBe "User summary for user ID <not specified> not found"
      }
    }
  }

  "my program working with EitherTs" should provideBetterErrorMessages(HelloEitherT.apply)

  private def provideBetterErrorMessages(program: MyProgram): Unit = {
    "find loyalty points balance for a user" in {
      program(Seq("1")).futureValue shouldBe "Peter, 33 with a balance of 5000 loyalty points."
    }

    "not find loyalty points balance" when {
      "loyalty programme can't be found" in {
        program(Seq("3")).futureValue shouldBe "Loyalty profile (ID: 12345) for user Thomas (ID 3) could not be found"
      }
      "user has not signed up for loyalty programme" in {
        program(Seq("4")).futureValue shouldBe "User Jane (ID 4) has not signed up for loyalty programme"
      }
      "non-existent user" in {
        program(Seq("10")).futureValue shouldBe "User ID 10 was not found"
      }
      "the input is invalid" in {
        program(Seq("invalid")).futureValue shouldBe
          """Failed to parse provided input: java.lang.NumberFormatException: For input string: "invalid""""
      }
      "no input" in {
        program(Seq()).futureValue shouldBe "You must provide at least one argument"
      }
    }
  }

}
