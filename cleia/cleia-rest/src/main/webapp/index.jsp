<%--
  #%L
  Cleia
  %%
  Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the 
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public 
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<html>
    <head>
        <link rel="stylesheet" href="//code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
        <script src="//code.jquery.com/jquery-1.9.1.min.js"></script>
        <script src="//code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
    </head>
    <body>
        <div data-role="header">
            <h1>Resumen del lumbago</h1>
        </div>
        <form action="complete" method="POST" enctype="multipart/form-data" data-ajax="false">
            <div data-role="collapsible-set" data-theme="c" data-content-theme="d" data-mini="true">
                <div data-role="collapsible" data-collapsed="false">
                    <h3>Datos demogr&aacute;ficos</h3>
                    <div>
                        <label for="user">Paciente: </label>
                        <label id="user">${patient_username}</label>
                    </div>
                    <div>
                        <label for="birthday">Edad: </label>
                        <label id="birthday">${patient_birthday}</label>
                    </div>
                    <div>
                        <label for="genre">Sexo: </label>
                        <label id="genre">${patient_genre}</label>
                    </div>
                    <div>
                        <label for="job">Profesi&oacute;n: </label>
                        <label id="job">${patient_job}</label>
                    </div>
                </div>
                <div data-role="collapsible">
                    <h3>Datos cl&iacute;nicos.</h3>
                    <#if patient_diagnosis??>
                    <div>
                        <label for="patient_diagnosis">Su m&eacute;dico le ha diagnosticado: </label>
                        <label id="patient_diagnosis">${patient_diagnosis}</label>
                    </div>
                    </#if>                    
                    <label>Su m&eacute;dico le ha practicado: </label>
                    <#if patient_diagnosis_interrogation??>
                    <div>
                        <label for="patient_diagnosis_interrogation">Interrogatorio: </label>
                        <label id="patient_diagnosis_interrogation">${patient_diagnosis_interrogation?string}</label>
                    </div>
                    </#if>
                    <#if patient_diagnosis_exploration??>
                    <div>
                        <label for="patient_diagnosis_exploration">Exploraci&oacute;n f&iacute;sica: </label>
                        <label id="patient_diagnosis_exploration">${patient_diagnosis_exploration?string}</label>
                    </div>
                    </#if>
                    <#if patient_diagnosis_test??>
                    <div>
                        <label for="patient_diagnosis_test">Pruebas de imagen:</label>
                        <label id="patient_diagnosis_test">${patient_diagnosis_test?string}</label>
                        <div  style="margin-left: 5px">
                            <#if patient_diagnosis_test_xray??>
                            <div>
                                <label for="patient_diagnosis_test_xray">Radiograf&iacute;as:</label>
                                <label id="patient_diagnosis_test_xray">${patient_diagnosis_test_xray?string}</label>
                            </div>
                            </#if>
                            <#if patient_diagnosis_test_tac??>
                            <div>
                                <label for="patient_diagnosis_test_tac">Tomograf&iacute;a Axial Computerizada (TAC):</label>
                                <label id="patient_diagnosis_test_tac">${patient_diagnosis_test_tac?string}</label>
                            </div>
                            </#if>
                            <#if patient_diagnosis_test_mri??>
                            <div>
                                <label for="patient_diagnosis_test_mri">Resonancia Magn&eacute;tica:</label>
                                <label id="patient_diagnosis_test_mri">${patient_diagnosis_test_mri?string}</label>
                            </div>
                            </#if>
                            <#if patient_diagnosis_test_bone_seintigraphy??>
                            <div>
                                <label for="patient_diagnosis_test_bone_seintigraphy">Gammagraf&iacute;a &oacute;sea:</label>
                                <label id="patient_diagnosis_test_bone_seintigraphy">${patient_diagnosis_test_bone_seintigraphy?string}</label>
                            </div>
                            </#if>
                        </div>
                    </div>
                    </#if>
                    <#if patient_diagnosis_test_blood??>
                    <div>
                        <label for="patient_diagnosis_test_blood">An&aacute;lisis de sangre y/o de orina:</label>
                        <label id="patient_diagnosis_test_blood">${patient_diagnosis_test_blood?string}</label>
                    </div>
                    </#if>
                    <#if patient_diagnosis_test_electromyography??>
                    <div>
                        <label for="patient_diagnosis_test_electromyography">Electromiograf&iacute;a:</label>
                        <label id="patient_diagnosis_test_electromyography">${patient_diagnosis_test_electromyography?string}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>Actividades cotidianas</h3>  
                    <#if patient_sendentary??>
                    <div>
                        <label for="patient_sendentary">Sedentario:</label>
                        <label id="patient_sendentary">${patient_sendentary?string}</label>
                    </div>
                    </#if>
                    <#if patient_active??>
                    <div>
                        <label for="patient_active">Activo:</label>
                        <label id="patient_active">${patient_active?string}</label>
                        <div style="margin-left: 5px">
                            <#if patient_active_grade??>
                            <div>
                                <label for="patient_active_grade">Nivel de actividad:</label>
                                <label id="patient_active_grade">${patient_active_grade}</label>
                            </div>
                            </#if>
                        </div>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">                    
                    <h3>Localizaci&oacute;n del dolor</h3>   
                    <#if patient_pain_time??>
                    <div>
                        <label for="patient_pain_time">El dolor lumbar que usted siente, lo tiene:</label>
                        <label id="patient_pain_time">${patient_pain_time}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>El dolor comenz&oacute;</h3>     
                    <#if patient_pain_way??>
                    <div>
                        <label for="patient_pain_way">El dolor comenz&oacute;:</label>
                        <label id="patient_pain_way">${patient_pain_way}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>El dolor</h3>        
                    <#if patient_pain_type_1??>
                    <div>
                        <label for="patient_pain_type_1">El dolor:</label>
                        <label id="patient_pain_type_1">${patient_pain_type_1}</label>
                    </div>
                    </#if>
                    <#if patient_pain_type_2??>
                    <div>
                        <label for="patient_pain_type_2">El dolor:</label>
                        <label id="patient_pain_type_2">${patient_pain_type_2}</label>
                    </div>
                    </#if>
                    <#if patient_pain_type_3??>
                    <div>
                        <label for="patient_pain_type_3">El dolor,al estar de pie, quieto:</label>
                        <label id="patient_pain_type_3">${patient_pain_type_3}</label>
                    </div>
                    </#if>
                    <#if patient_pain_type_4??>
                    <div>
                        <label for="patient_pain_type_4">El dolor, al realizar determinados ejecicios o adoptar determinadas posturas:</label>
                        <label id="patient_pain_type_4">${patient_pain_type_4}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>El dolor es</h3>   
                    <#if patient_pain_type_night??>
                    <div>
                        <label for="patient_pain_type_night">Sobretodo nocturno:</label>
                        <label id="patient_pain_type_night">${patient_pain_type_night?string}</label>
                        <div  style="margin-left: 5px">
                            <#if patient_pain_type_night_value??>
                            <div>
                                <label for="patient_pain_type_night_value">Cuando:</label>
                                <label id="patient_pain_type_night_value">${patient_pain_type_night_value}</label>
                            </div>
                            </#if>                            
                        </div>
                    </div>
                    </#if>
                    <#if patient_pain_type_day??>
                    <div>
                        <label for="patient_pain_type_day">Sobretodo nocturno:</label>
                        <label id="patient_pain_type_day">${patient_pain_type_day?string}</label>
                        <div  style="margin-left: 5px">
                            <#if patient_pain_type_day_value??>
                            <div>
                                <label for="patient_pain_type_day_value">Cuando:</label>
                                <label id="patient_pain_type_day_value">${patient_pain_type_day_value}</label>
                            </div>
                            </#if>                            
                        </div>
                    </div>
                    </#if>
                    <#if patient_pain_type_both??>
                    <div>
                        <label for="patient_pain_type_both">Diurno y nocturno, continuo, con la misma intensidad:</label>
                        <label id="patient_pain_type_both">${patient_pain_type_both?string}</label>
                        <div  style="margin-left: 5px">
                            <#if patient_pain_localization_back??>
                            <div>
                                <label for="patient_pain_localization_back">Fijo en la parte baja de la espalda:</label>
                                <label id="patient_pain_localization_back">${patient_pain_localization_back?string}</label>
                            </div>
                            </#if>                            
                            <#if patient_pain_localization_leg??>
                            <div>
                                <label for="patient_pain_localization_leg">Se "corre" (irradia) hacia la nalga, el muslo y la pierna:</label>
                                <label id="patient_pain_localization_leg">${patient_pain_localization_leg?string}</label>
                            </div>
                            </#if>
                            <#if patient_pain_localization_buttock??>
                            <div>
                                <label for="patient_pain_localization_buttock">Fijo en la nalga:</label>
                                <label id="patient_pain_localization_buttock">${patient_pain_localization_buttock?string}</label>
                                <div  style="margin-left: 10px">
                                    <#if patient_pain_localization_buttock_one_side??>
                                    <div>
                                        <label for="patient_pain_localization_buttock_one_side">Unas veces a la derecha, y otras a la izquierda:</label>
                                        <label id="patient_pain_localization_buttock_one_side">${patient_pain_localization_buttock_one_side?string}</label>
                                    </div>
                                    </#if>                            
                                </div>
                            </div>
                            </#if>
                            <#if patient_pain_localization_high_with_sneeze??>
                            <div>
                                <label for="patient_pain_localization_high_with_sneeze">Aumenta con la tos, estornudos y con otras maniobras como al hacer fuerza en al deposistaci&oacute;n:</label>
                                <label id="patient_pain_localization_high_with_sneeze">${patient_pain_localization_high_with_sneeze?string}</label>
                            </div>
                            </#if>
                        </div>
                    </div>
                    </#if>
                    <#if patient_pain_scale??>
                    <div>
                        <label for="patient_pain_scale">Tipo:</label>
                        <label id="patient_pain_scale">${patient_pain_scale}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>Clasificaci&oacute;n del dolor</h3>   
                    <#if patient_pain_first_time??>
                    <div>
                        <label for="patient_pain_first_time">&iquest;Es la primera vez que usted tiene este dolor?</label>
                        <label id="patient_pain_first_time">${patient_pain_first_time?string}</label>
                    </div>
                    </#if>                    
                    <#if patient_pain_like_others??>
                    <div>
                        <label for="patient_pain_like_others">&iquest;Tiene las mismas caracter&iacute;sticas que la/s veces anteriores?</label>
                        <label id="patient_pain_like_others">${patient_pain_like_others?string}</label>
                    </div>
                    </#if> 
                    <#if patient_pain_intensity_max??>
                    <div>
                        <label for="patient_pain_intensity_max">Intensidad <b>m&aacute;xima</b> del dolor sentido en las &uacute;ltimas 24 horas. 10 igual al peor dolor y 0 nig&uacute;n dolor</label>
                        <label id="patient_pain_intensity_max">${patient_pain_intensity_max}</label>
                    </div>
                    </#if> 
                    <#if patient_pain_intensity_max??>
                    <div>
                        <label for="patient_pain_intensity_min">Intensidad <b>m&iacute;nima</b> del dolor sentido en las &uacute;ltimas 24 horas. 10 igual al peor dolor y 0 nig&uacute;n dolor</label>
                        <label id="patient_pain_intensity_min">${patient_pain_intensity_min}</label>
                    </div>
                    </#if>
                    <#if patient_pain_intensity_average??>
                    <div>
                        <label for="patient_pain_intensity_average">Intensidad <b>media</b> del dolor sentido en las &uacute;ltimas 24 horas. 10 igual al peor dolor y 0 nig&uacute;n dolor</label>
                        <label id="patient_pain_intensity_average">${patient_pain_intensity_average}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>Medicaci&oacute;n</h3>   
                    <#if patient_better_medication??>
                    <div>
                        <label for="patient_better_medication">medicaci&oacute;n m&aacute;s eficaz:</label>
                        <label id="patient_better_medication">${patient_better_medication}</label>
                    </div>
                    </#if>
                </div>
                <div data-role="collapsible">
                    <h3>Antecedentes Personales y familiares</h3>
                    <#if patient_record??>
                    <div>
                        <label for="patient_record">Paciente padece o ha padecido:</label>
                        <div id="patient_record">
                            <#list patient_record as record>
                            <label>${record}</label>
                            </#list>
                        </div>
                    </div>
                    </#if>
                    <#if patient_family_record??>
                    <div>
                        <label for="patient_family_record">Familiares directos padecen o han padecido:</label>
                        <div id="patient_family_record">
                            <#list patient_family_record as record>
                            <label>${record}</label>
                            </#list>
                        </div>
                    </div>
                    </#if>                    
                </div>                
                <div data-role="collapsible">
                    <h3>CONCLUSIONES</h3>
                    <label for="patient_result"></label>
                    <select name="patient_result" id="patient_result">
                        <option value="Nada">Nada</option>
                        <option value="Lumbalg&iacute;a mec&aacute;nica aguda">Lumbalg&iacute;a mec&aacute;nica aguda</option>
                        <option value="Lumbalg&iacute;a mec&aacute;nica subaguda">Lumbalg&iacute;a mec&aacute;nica subaguda</option>
                        <option value="Lumbalg&iacute;a mec&aacute;nica cr&oacute;nica">Lumbalg&iacute;a mec&aacute;nica cr&oacute;nica</option>
                        <option value="Lumboci&aacute;tica (Lumbociatolog&iacute;a)">Lumboci&aacute;tica (Lumbociatolog&iacute;a)</option>
                        <option value="Lumbalg&iacute;a inflamatoria">Lumbalg&iacute;a inflamatoria</option>
                    </select>
                    <label for="textarea-1">Comentario m&eacute;dico:</label>
                    <textarea cols="40" rows="8" name="patient_result_report" id="textarea-1"></textarea>
                </div>
            </div>  
            <div class=""><input name="submit" type="submit" value="Enviar" id="sb" /></div>
        </form>
    </body>
</html>
