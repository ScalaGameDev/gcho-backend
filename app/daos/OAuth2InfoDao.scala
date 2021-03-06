package daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import reactivemongo.api.DefaultDB
import reactivemongo.bson.{BSONDocument, BSONDocumentHandler, Macros}

import scala.concurrent.{ExecutionContext, Future}

trait OAuth2InfoDao extends DelegableAuthInfoDAO[OAuth2Info]

class MongoOAuth2InfoDao(override val mongo: Future[DefaultDB])(override implicit val ec: ExecutionContext)
  extends OAuth2InfoDao with BSONMongoDao {
  scribe debug "Instantiating."
  override val colName          : String                          = "passwords"
  implicit val loginInfoHandler : BSONDocumentHandler[LoginInfo]  = Macros.handler[LoginInfo]
  implicit val oauth2InfoHandler: BSONDocumentHandler[OAuth2Info] = Macros.handler[OAuth2Info]
  val LGN = "login"
  val PSW = "paswd"

  override def find(loginInfo: LoginInfo): Future[Option[OAuth2Info]] = for {
    c <- storage
    b <- c.find(d :~ LGN -> loginInfo).one[BSONDocument]
  } yield b.flatMap(_.getAs[OAuth2Info](PSW))

  override def add(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = for {
    c <- storage
    _ <- c insert d :~ LGN -> loginInfo :~ PSW -> authInfo
  } yield authInfo

  override def update(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = for {
    c <- storage
    _ <- c.update(d :~ LGN -> loginInfo, d :~ "$set" -> (d :~ PSW -> authInfo))
  } yield authInfo

  override def save(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = for {
    c <- storage
    _ <- c.update(d :~ LGN -> loginInfo, d :~ "$set" -> (d :~ PSW -> authInfo), upsert = true)
  } yield authInfo

  override def remove(loginInfo: LoginInfo): Future[Unit] = for {
    c <- storage
    _ <- c remove d :~ LGN -> loginInfo
  } yield ()
}
