/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.data.localization.Resource

object R {
    val field_activated_hint= Resource(
        "Activated",
        "Aktywowany"
    )

    val field_invitation_hint = Resource(
        "Scan this QR code with your smartphone and follow the instructions to activate this channel",
        "Zeskanuj kod telefonem i podążaj za instrukcjami aby aktywawać kanał"
    )

    val mobile_credentials_title = Resource(
        "Mobile credentials",
        "Poświadczenia mobilne"
    )

    val mobile_credentials_description = Resource(
        "Contains a list of allowed mobile credentials (of devices that can connect remotely).",
        "Zawiera listę poświadczeń mobilnych (urządzeń, które mogą się łączyć zdalnie)."
    )

    var plugin_description = Resource(
        "Allows to control devices from an Android mobile phone. The connection is secured by SaltChannel.",
        "Pozwala na kontrolowanie urządzeń za pomocą smartfona z Androidem. Połączenie jest zabezpieczone przez SaltChannel"
    )

    var plugin_name = Resource("Mobile access", "Dostęp mobilny")
}