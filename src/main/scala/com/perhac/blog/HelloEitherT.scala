package com.perhac.blog

import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HelloEitherT {

  import AllRepositories._

  def apply(args: String*): Future[String] = {

    val optUserId: Either[String, Int] =
      args.headOption.toRight("You must provide at least one argument") >>= stringToIntEither

    def notSignedUp(user: User): String =
      s"User ${user.name} (ID ${user.id}) has not signed up for loyalty programme"

    def lpnf(loyaltyProfileId: Int, user: User): String =
      s"Loyalty profile (ID: $loyaltyProfileId) for user ${user.name} (ID ${user.id}) could not be found"

    def action: EitherT[Future, String, SimpleUserProfile] =
      for {
        userId           <- EitherT.fromEither[Future](optUserId)
        _                <- EitherT.rightT[Future, String]("this is not important")
        user             <- findUserById(userId).toRight(s"User ID $userId was not found")
        userProfile      <- EitherT.right[String](findUserProfileByUserId(user.id))
        loyaltyProfileId <- EitherT.fromOption[Future](userProfile.loyaltyProfileId, notSignedUp(user))
        loyaltyProfile   <- EitherT.fromOptionF(findLoyaltyProfileById(loyaltyProfileId), lpnf(loyaltyProfileId, user))
        _                <- EitherT.right[String](updateLoyaltyProfile(loyaltyProfile))
      } yield
        SimpleUserProfile(
          name = user.name,
          age = userProfile.age,
          pointsBalance = loyaltyProfile.pointsBalance
        )

    action.map { u =>
      s"${u.name}, ${u.age} with a balance of ${u.pointsBalance} loyalty points."
    }.merge

  }

  /* --------------------------------------------------- */

  def findLoyaltyProfile(userProfile: UserProfile): Future[Either[String, LoyaltyProfile]] =
    EitherT
      .fromOption[Future](userProfile.loyaltyProfileId, "user didn't sign up")
      .flatMap(id => EitherT.fromOptionF(findLoyaltyProfileById(id), "profile not found"))
      .value

  def updateLoyaltyProfile(profile: LoyaltyProfile): Future[Unit] =
    saveLoyaltyProfile(profile.copy(notes = "✔".some))

  def touch(profileId: Int): EitherT[Future, String, Unit] =
    EitherT.fromOptionF(findLoyaltyProfileById(profileId), "profile not found").semiflatMap(updateLoyaltyProfile)

  def loyaltyProfileNotes(profileId: Int): EitherT[Future, String, String] =
    EitherT
      .fromOptionF(findLoyaltyProfileById(profileId), "profile not found")
      .subflatMap(_.notes.toRight("<no notes yet>"))

  def findLoyaltyProfileByUserId(userId: Int): EitherT[Future, String, LoyaltyProfile] =
    EitherT.right[String](findUserProfileByUserId(userId)).flatMapF(findLoyaltyProfile)

}
