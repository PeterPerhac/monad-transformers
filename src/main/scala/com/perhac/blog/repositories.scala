package com.perhac.blog

import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UserRepository {

  private val users = List(
    User(id = 1, name = "Peter", email = "peter@perhac.com"),
    User(id = 2, name = "Bob", email = "bob@email.com"),
    User(id = 3, name = "Thomas", email = "thomas@email.com")
  )

  def findUserById(id: Int): Future[Option[User]] =
    users.find(_.id == id).pure[Future]

}
object UserRepository extends UserRepository

trait UserProfileRepository {

  private val userProfiles = Map(
    1 -> UserProfile(loyaltyProfileId = Some(10001), active = true, age = 33),
    2 -> UserProfile(loyaltyProfileId = Some(10002), active = true, age = 36),
    3 -> UserProfile(loyaltyProfileId = None, active = true, age = 39)
  )

  def findUserProfileByUserId(userId: Int): Future[UserProfile] =
    // this Map#apply might explode; however, all data is hard-coded, nothing can go wrong.
    // Trust me, I'm an engineer.™
    userProfiles(userId).pure[Future]

}

object UserProfileRepository extends UserProfileRepository

trait LoyaltyProfileRepository {

  private val loyaltyProfiles = List(
    LoyaltyProfile(id = 10001, pointsBalance = 5000),
    LoyaltyProfile(id = 10002, pointsBalance = 10000)
  )

  def findLoyaltyProfileById(id: Int): Future[Option[LoyaltyProfile]] =
    loyaltyProfiles.find(_.id == id).pure[Future]

  def saveLoyaltyProfile(loyaltyProfile: LoyaltyProfile): Future[Unit] =
    ().pure[Future]

}

object LoyaltyProfileRepository extends LoyaltyProfileRepository

trait AllRepositories extends UserRepository with UserProfileRepository with LoyaltyProfileRepository
object AllRepositories extends AllRepositories
