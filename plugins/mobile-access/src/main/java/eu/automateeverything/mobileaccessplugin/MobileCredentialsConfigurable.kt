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

import eu.automateeverything.data.Repository
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.domain.settings.SettingsResolver
import org.pf4j.Extension
import saltchannel.CryptoLib
import saltchannel.util.Hex
import saltchannel.util.Rand
import java.security.SecureRandom

@Extension
class MobileCredentialsConfigurable(
    private val repository: Repository,
    private val settingsResolver: SettingsResolver
) : GeneratedConfigurable() {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: LinkedHashMap<String, FieldDefinition<*>> = LinkedHashMap(super.fieldDefinitions)
            result[FIELD_ACTIVATED] = activatedField
            result[FIELD_PUBKEY] = pubKeyField
            result[FIELD_QR_CODE] = qrCodeField
            return result
        }

    override val addNewRes: Resource
        get() = Resource.createUniResource("-")

    override val editRes: Resource
        get() = Resource.createUniResource("-")

    override val parent: Class<out Configurable>? = null

    override val titleRes = R.mobile_credentials_title

    override val descriptionRes = R.mobile_credentials_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Mobile User by TukTuk Design from NounProject.com</title>
              <g id="svg_25">
               <path d="m65.04472,56.19962l-30.08944,0c-0.79578,0 -1.44103,-0.64657 -1.44103,-1.44103c0,-0.79644 0.64525,-1.44235 1.44103,-1.44235l30.08944,0c0.79644,0 1.44103,0.64525 1.44103,1.44235c0,0.79578 -0.64459,1.44103 -1.44103,1.44103z" id="svg_26"/>
               <path d="m65.04472,62.92578l-30.08944,0c-0.79578,0 -1.44103,-0.64525 -1.44103,-1.44103c0,-0.79644 0.64525,-1.44103 1.44103,-1.44103l30.08944,0c0.79644,0 1.44103,0.64459 1.44103,1.44103c0,0.79578 -0.64459,1.44103 -1.44103,1.44103z" id="svg_27"/>
               <path d="m65.04472,69.65023l-30.08944,0c-0.79578,0 -1.44103,-0.64525 -1.44103,-1.44103c0,-0.79644 0.64525,-1.44103 1.44103,-1.44103l30.08944,0c0.79644,0 1.44103,0.64459 1.44103,1.44103c0,0.79512 -0.64459,1.44103 -1.44103,1.44103z" id="svg_28"/>
               <path d="m65.04472,76.37639l-30.08944,0c-0.79578,0 -1.44103,-0.64657 -1.44103,-1.44235c0,-0.79644 0.64525,-1.44103 1.44103,-1.44103l30.08944,0c0.79644,0 1.44103,0.64459 1.44103,1.44103c0,0.79578 -0.64459,1.44235 -1.44103,1.44235z" id="svg_29"/>
               <path d="m69.90174,4.36631l-39.80348,0c-3.57766,0 -6.47626,2.89927 -6.47626,6.47626l0,78.31674c0,3.57766 2.8986,6.47439 6.47626,6.47439l39.80348,0c3.5763,0 6.47626,-2.8986 6.47626,-6.47439l-0.00066,-78.31674c0,-3.57562 -2.89927,-6.47626 -6.47507,-6.47626l-0.00052,0l-0.00001,0zm-23.23256,3.38277l6.6613,0c0.55638,0 1.00667,0.45161 1.00667,1.00667c0,0.55505 -0.45093,1.00667 -1.00667,1.00667l-6.6613,0c-0.55505,0 -1.006,-0.45161 -1.006,-1.00667c0,-0.55505 0.45027,-1.00667 1.006,-1.00667zm3.33031,84.93424c-1.59024,0 -2.88145,-1.29182 -2.88145,-2.88264c0,-1.58891 1.29116,-2.87805 2.88145,-2.87805c1.59024,0 2.88264,1.28917 2.88264,2.87805c-0.00066,1.5909 -1.29248,2.88264 -2.88264,2.88264zm20.97806,-8.71242l-41.95952,0l0,-70.82663l41.95952,0l0,70.82663z" id="svg_30"/>
               <path d="m54.38847,38.8325c-0.08289,-0.03714 -0.18834,-0.07692 -0.30638,-0.12069l-0.10478,-0.03913c-0.64459,-0.23674 -1.61742,-0.59485 -1.75471,-1.12206c-0.19496,-0.74538 0.28117,-1.36609 0.75202,-1.97744c0.18369,-0.2394 0.35811,-0.46752 0.49073,-0.70161c0.52388,-0.93438 0.91051,-2.13804 0.98677,-3.06768c0.13794,-1.68108 -0.2646,-3.08042 -1.15985,-4.04927c-0.78318,-0.85214 -1.89205,-1.30243 -3.20502,-1.30243c-0.22547,0 -0.45757,0.01326 -0.68835,0.03979c-2.1944,0.25266 -3.65066,1.84893 -3.80583,4.16794c-0.12335,1.81973 0.38595,3.48616 1.55907,5.09571c0.07295,0.1008 0.14921,0.19763 0.22083,0.28781c0.29444,0.37467 0.54843,0.69698 0.49935,1.19964c-0.063,0.64922 -0.70891,0.89525 -1.22617,1.09221l-0.21088,0.08157c-0.62204,0.25002 -1.30045,0.62535 -1.67512,0.83158l-0.09151,0.04974c-1.20628,0.66846 -2.51526,1.46953 -2.81049,2.55584c-0.20824,0.76329 -0.14523,1.39063 0.18635,1.9204c0.62932,1.00268 2.12735,1.382 3.19976,1.58095c1.55907,0.28714 3.28193,0.31101 4.77333,0.31101c3.4006,0 7.39486,-0.2374 8.10839,-2.08762c0.22083,-0.56566 0.12069,-1.47948 0.00199,-1.83093c-0.49737,-1.48943 -2.60626,-2.41714 -3.73945,-2.91523l-0.00005,-0.00009l0,-0.00001z" id="svg_31"/>
              </g>
             </g>
            </svg>
        """.trimIndent()

    private val qrCodeField = QrCodeField(FIELD_QR_CODE, R.field_invitation_hint, 0, "")
    private val activatedField = BooleanField(FIELD_ACTIVATED, R.field_activated_hint, false)
    private val pubKeyField = StringField(FIELD_PUBKEY, R.field_public_key, 32,"")

    override fun generate(): InstanceDto {
        val pluginSettings = settingsResolver.resolve()
        val secretsPassword:String = if (pluginSettings.size == 1) {
            pluginSettings[0].fields[SecretsProtectionSettingGroup.FIELD_PASSWORD]!!
        } else {
            SecretsProtectionSettingGroup.DEFAULT_PASSWORD
        }

        val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }
        val keyPair = CryptoLib.createSigKeys(random)
        val pubKeyHexString = String(Hex.toHexCharArray(keyPair.pub(), 0, keyPair.pub().size))

        val bindingIdBytes = ByteArray(10) { 0.toByte() }
        random.randomBytes(bindingIdBytes)
        val bindingIdHexString = String(Hex.toHexCharArray(bindingIdBytes, 0, bindingIdBytes.size))

        val nameField = Pair(FIELD_NAME, "#ID: $bindingIdHexString")
        val descriptionField = Pair(FIELD_DESCRIPTION, null)
        val qrCodeField = Pair(FIELD_QR_CODE, "http://automateeverything.eu?bindingId=$bindingIdHexString&pubkey=$pubKeyHexString")
        val activatedField = Pair(FIELD_ACTIVATED, false.toString())
        val pubKeyField = Pair(FIELD_PUBKEY, pubKeyHexString)

        val storage = SecretStorage()
        storage.storeSecret(secretsPassword, pubKeyHexString, keyPair.sec())

        val newInstance = InstanceDto(0, null, listOf(), MobileCredentialsConfigurable::class.java.name,
            mapOf(nameField, descriptionField, qrCodeField, activatedField, pubKeyField), null)
        val newId = repository.saveInstance(newInstance)
        newInstance.id = newId

        return newInstance
    }

    companion object {
        const val FIELD_QR_CODE = "qrCode"
        const val FIELD_ACTIVATED = "activated"
        const val FIELD_PUBKEY = "pubkey"
    }
}