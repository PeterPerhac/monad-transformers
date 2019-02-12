package com.perhac.blog

import cats.data.OptionT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HelloOptionT2 {

  import AllRepositories._
  import PixieDust._

  def apply(args: String*): Future[String] = {

    val optUserId: Option[Int] = args.headOption >>= stringToIntOption

    def action: OptionT[Future, SimpleUserProfile] =
      // format: off
      for {
        userId           <- optUserId                                 |> liftOption
        _                <- "this is not important"                   |> lift
        user             <- findUserById(userId)                      |> liftFutureOption
        userProfile      <- findUserProfileByUserId(user.id)          |> liftFuture
        loyaltyProfileId <- userProfile.loyaltyProfileId              |> liftOption
        loyaltyProfile   <- findLoyaltyProfileById(loyaltyProfileId)  |> liftFutureOption
        _                <- updateLoyaltyProfile(loyaltyProfile)      |> liftFuture
      } yield SimpleUserProfile(
        name = user.name, 
        age = userProfile.age, 
        pointsBalance = loyaltyProfile.pointsBalance
      )
      // format: on

    action.fold(s"Loyalty profile for user ID ${args.headOption.getOrElse("<not specified>")} not found") { u =>
      s"${u.name}, ${u.age} with a balance of ${u.pointsBalance} loyalty points."
    }

  }

  /* --------------------------------------------------- */

  def findLoyaltyProfile(userProfile: UserProfile): Future[Option[LoyaltyProfile]] =
    (userProfile.loyaltyProfileId |> liftOption).flatMapF(findLoyaltyProfileById).value

  def updateLoyaltyProfile(profile: LoyaltyProfile): Future[Unit] =
    saveLoyaltyProfile(profile.copy(notes = "✔".some))

  def touch(profileId: Int): OptionT[Future, Unit] =
    (findLoyaltyProfileById(profileId) |> liftFutureOption).semiflatMap(updateLoyaltyProfile)

  def loyaltyProfileNotes(profileId: Int): OptionT[Future, String] =
    (findLoyaltyProfileById(profileId) |> liftFutureOption).subflatMap(_.notes)

  def findLoyaltyProfileByUserId(userId: Int): OptionT[Future, LoyaltyProfile] =
    OptionT.liftF(findUserProfileByUserId(userId)).flatMapF(findLoyaltyProfile)

}
