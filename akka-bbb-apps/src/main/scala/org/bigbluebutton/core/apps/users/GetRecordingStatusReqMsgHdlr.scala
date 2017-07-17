package org.bigbluebutton.core.apps.users

import org.bigbluebutton.common2.msgs._
import org.bigbluebutton.core.OutMessageGateway
import org.bigbluebutton.core.running.{ BaseMeetingActor, LiveMeeting }
import org.bigbluebutton.core2.MeetingStatus2x

trait GetRecordingStatusReqMsgHdlr {
  this: UsersApp =>

  val liveMeeting: LiveMeeting
  val outGW: OutMessageGateway

  def handleGetRecordingStatusReqMsg(msg: GetRecordingStatusReqMsg) {

    def buildGetRecordingStatusRespMsg(meetingId: String, userId: String, recording: Boolean): BbbCommonEnvCoreMsg = {
      val routing = Routing.addMsgToClientRouting(MessageTypes.DIRECT, meetingId, userId)
      val envelope = BbbCoreEnvelope(GetRecordingStatusRespMsg.NAME, routing)
      val body = GetRecordingStatusRespMsgBody(recording, userId)
      val header = BbbClientMsgHeader(GetRecordingStatusRespMsg.NAME, meetingId, userId)
      val event = GetRecordingStatusRespMsg(header, body)

      BbbCommonEnvCoreMsg(envelope, event)
    }

    val event = buildGetRecordingStatusRespMsg(liveMeeting.props.meetingProp.intId, msg.body.requestedBy,
      MeetingStatus2x.isRecording(liveMeeting.status))
    outGW.send(event)
  }
}
