package com.perhac.blog

import cats.data.OptionT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HelloOptionT {

  import AllRepositories._

  def apply(args: String*): Future[String] = {

    val optUserId: Option[Int] = args.headOption >>= stringToIntOption

    def action: OptionT[Future, SimpleUserProfile] =
      for {
        userId           <- OptionT.fromOption[Future](optUserId)
        _                <- OptionT.some[Future]("this is not important")
        user             <- OptionT(findUserById(userId))
        userProfile      <- OptionT.liftF(findUserProfileByUserId(user.id))
        loyaltyProfileId <- OptionT.fromOption[Future](userProfile.loyaltyProfileId)
        loyaltyProfile   <- OptionT(findLoyaltyProfileById(loyaltyProfileId))
        _                <- OptionT.liftF(updateLoyaltyProfile(loyaltyProfile))
      } yield
        SimpleUserProfile(
          name = user.name,
          age = userProfile.age,
          pointsBalance = loyaltyProfile.pointsBalance
        )

    action.fold(s"Loyalty profile for user ID ${args.headOption.getOrElse("<not specified>")} not found") { u =>
      s"${u.name}, ${u.age} with a balance of ${u.pointsBalance} loyalty points."
    }

  }

  /* --------------------------------------------------- */

  def findLoyaltyProfile(userProfile: UserProfile): Future[Option[LoyaltyProfile]] =
    OptionT.fromOption[Future](userProfile.loyaltyProfileId).flatMapF(findLoyaltyProfileById).value

  def updateLoyaltyProfile(profile: LoyaltyProfile): Future[Unit] =
    saveLoyaltyProfile(profile.copy(notes = "✔".some))

  def touch(profileId: Int): OptionT[Future, Unit] =
    OptionT(findLoyaltyProfileById(profileId)).semiflatMap(updateLoyaltyProfile)

  def loyaltyProfileNotes(profileId: Int): OptionT[Future, String] =
    OptionT(findLoyaltyProfileById(profileId)).subflatMap(_.notes)

  def findLoyaltyProfileByUserId(userId: Int): OptionT[Future, LoyaltyProfile] =
    OptionT.liftF(findUserProfileByUserId(userId)).flatMapF(findLoyaltyProfile)

}
