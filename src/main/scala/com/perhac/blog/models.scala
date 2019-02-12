package com.perhac.blog

case class User(id: Int, name: String, email: String)

case class UserProfile(loyaltyProfileId: Option[Int], active: Boolean, age: Int)

case class LoyaltyProfile(id: Int, pointsBalance: Int, notes: Option[String] = None)

case class SimpleUserProfile(name: String, age: Int, pointsBalance: Int)
