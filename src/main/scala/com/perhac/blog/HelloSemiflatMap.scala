package com.perhac.blog

import cats.data.OptionT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HelloSemiflatMap {

  import AllRepositories._

  def apply(args: String*): Future[String] = {

    val optUserId: Option[Int] = args.headOption >>= stringToIntOption

    def updateLoyaltyProfile(profile: LoyaltyProfile): OptionT[Future, Unit] =
      OptionT.liftF(saveLoyaltyProfile(profile.copy(notes = "✔".some)))

    def action: OptionT[Future, LoyaltyProfile] =
      OptionT
        .fromOption[Future](optUserId)
        .flatMapF(findUserById)
        .semiflatMap(user => findUserProfileByUserId(user.id))
        .subflatMap(_.loyaltyProfileId)
        .flatMapF(findLoyaltyProfileById)
        .flatTap(updateLoyaltyProfile)

    /* --------------------------------------------------- */

    def doesNotWork: OptionT[Future, SimpleUserProfile] =
      OptionT
        .fromOption[Future](optUserId)
        .flatMapF(findUserById)
        .semiflatMap(user => findUserProfileByUserId(user.id))
        .subflatMap(_.loyaltyProfileId)
        .flatMapF(findLoyaltyProfileById)
        .map { loyaltyProfile =>
          SimpleUserProfile( //
            name = ???, // can't access user, so don't know `name`
            age = ???, // can't access userProfile, so don't know `age`
            pointsBalance = loyaltyProfile.pointsBalance //
          )
        }

    action.fold("Operation could not be performed")(_ => "Loyalty Profile updated")

  }
}
