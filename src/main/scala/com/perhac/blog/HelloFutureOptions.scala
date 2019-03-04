package com.perhac.blog

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HelloFutureOptions {

  import AllRepositories._

  def apply(args: String*): Future[String] = {

    val optUserId: Option[Int] = args.headOption.flatMap(stringToIntOption)

    def action: Future[Option[SimpleUserProfile]] =
      optUserId.fold(Future.successful(Option.empty[SimpleUserProfile])) { userId =>
        findUserById(userId).value.flatMap {
          case Some(user) =>
            findUserProfileByUserId(user.id).flatMap { userProfile =>
              userProfile.loyaltyProfileId.fold(Future.successful(Option.empty[SimpleUserProfile])) {
                loyaltyProfileId =>
                  findLoyaltyProfileById(loyaltyProfileId).flatMap {
                    case Some(loyaltyProfile) =>
                      updateLoyaltyProfile(loyaltyProfile).map { _ =>
                        Some(
                          SimpleUserProfile(
                            name = user.name,
                            age = userProfile.age,
                            pointsBalance = loyaltyProfile.pointsBalance))
                      }
                    case None => Future.successful(None)
                  }
              }
            }
          case None => Future.successful(None)
        }
      }

    action.map { optProfile =>
      optProfile.fold(s"User summary for user ID ${args.headOption.getOrElse("<not specified>")} not found") { u =>
        s"${u.name}, ${u.age} with a balance of ${u.pointsBalance} loyalty points."
      }
    }
  }

  /* --------------------------------------------------- */

  def findLoyaltyProfile(userProfile: UserProfile): Future[Option[LoyaltyProfile]] =
    userProfile.loyaltyProfileId.fold(Future.successful(Option.empty[LoyaltyProfile]))(findLoyaltyProfileById)

  def updateLoyaltyProfile(profile: LoyaltyProfile): Future[Unit] =
    saveLoyaltyProfile(profile.copy(notes = Some("✔")))

  def touch(profileId: Int): Future[Option[Unit]] =
    findLoyaltyProfileById(profileId).flatMap {
      case Some(loyaltyProfile) => updateLoyaltyProfile(loyaltyProfile).map(_ => Some(()))
      case None                 => Future.successful(None)
    }

  def loyaltyProfileNotes(profileId: Int): Future[Option[String]] =
    findLoyaltyProfileById(profileId).map {
      case Some(loyaltyProfile) => loyaltyProfile.notes
      case None                 => None
    }

  def findLoyaltyProfileByUserId(userId: Int): Future[Option[LoyaltyProfile]] =
    findUserProfileByUserId(userId).flatMap(findLoyaltyProfile)

}
