/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU Affero GPL v3 http://www.gnu.org/licenses/agpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2018
 * 
 */
//----------------------------------------------------------
var conversions = new Object();
conversions['ae'] = 'ä|æ|ǽ';
conversions['oe'] = 'ö|œ';
conversions['ue'] = 'ü';
conversions['Ae'] = 'Ä';
conversions['Ue'] = 'Ü';
conversions['Oe'] = 'Ö';
conversions['A'] = 'À|Á|Â|Ã|Ä|Å|Ǻ|Ā|Ă|Ą|Ǎ';
conversions['a'] = 'à|á|â|ã|å|ǻ|ā|ă|ą|ǎ|ª';
conversions['C'] = 'Ç|Ć|Ĉ|Ċ|Č';
conversions['c'] = 'ç|ć|ĉ|ċ|č';
conversions['D'] = 'Ð|Ď|Đ';
conversions['d'] = 'ð|ď|đ';
conversions['E'] = 'È|É|Ê|Ë|Ē|Ĕ|Ė|Ę|Ě';
conversions['e'] = 'è|é|ê|ë|ē|ĕ|ė|ę|ě';
conversions['G'] = 'Ĝ|Ğ|Ġ|Ģ';
conversions['g'] = 'ĝ|ğ|ġ|ģ';
conversions['H'] = 'Ĥ|Ħ';
conversions['h'] = 'ĥ|ħ';
conversions['I'] = 'Ì|Í|Î|Ï|Ĩ|Ī|Ĭ|Ǐ|Į|İ';
conversions['i'] = 'ì|í|î|ï|ĩ|ī|ĭ|ǐ|į|ı';
conversions['J'] = 'Ĵ';
conversions['j'] = 'ĵ';
conversions['K'] = 'Ķ';
conversions['k'] = 'ķ';
conversions['L'] = 'Ĺ|Ļ|Ľ|Ŀ|Ł';
conversions['l'] = 'ĺ|ļ|ľ|ŀ|ł';
conversions['N'] = 'Ñ|Ń|Ņ|Ň';
conversions['n'] = 'ñ|ń|ņ|ň|ŉ';
conversions['O'] = 'Ò|Ó|Ô|Õ|Ō|Ŏ|Ǒ|Ő|Ơ|Ø|Ǿ';
conversions['o'] = 'ò|ó|ô|õ|ō|ŏ|ǒ|ő|ơ|ø|ǿ|º';
conversions['R'] = 'Ŕ|Ŗ|Ř';
conversions['r'] = 'ŕ|ŗ|ř';
conversions['S'] = 'Ś|Ŝ|Ş|Š';
conversions['s'] = 'ś|ŝ|ş|š|ſ';
conversions['T'] = 'Ţ|Ť|Ŧ';
conversions['t'] = 'ţ|ť|ŧ';
conversions['U'] = 'Ù|Ú|Û|Ũ|Ū|Ŭ|Ů|Ű|Ų|Ư|Ǔ|Ǖ|Ǘ|Ǚ|Ǜ';
conversions['u'] = 'ù|ú|û|ũ|ū|ŭ|ů|ű|ų|ư|ǔ|ǖ|ǘ|ǚ|ǜ';
conversions['Y'] = 'Ý|Ÿ|Ŷ';
conversions['y'] = 'ý|ÿ|ŷ';
conversions['W'] = 'Ŵ';
conversions['w'] = 'ŵ';
conversions['Z'] = 'Ź|Ż|Ž';
conversions['z'] = 'ź|ż|ž';
conversions['AE'] = 'Æ|Ǽ';
conversions['ss'] = 'ß';
conversions['IJ'] = 'Ĳ';
conversions['ij'] = 'ĳ';
conversions['OE'] = 'Œ';
conversions['f'] = 'ƒ';
//-------------------------------------------------    
function UnifyChars(str)
{
for(var i in conversions)
    {
    var re = new RegExp(conversions[i],"g");
    str = str.replace(re,i);
    }
return str;
}
//-------------------------------------------------    
var Filt="";
var BCK="Backspace";
function Search(event)
{
var Tec=event.key;
var first=false;
if (Tec==BCK)
    {
    if (Filt.length>0)
       Filt=Filt.substr(0, Filt.length-1)     
    }   
else if (Tec.length!=1)
    {
//    document.getElementById("demo").innerHTML = "Filter="+Filt+"<br> Tec="+Tec;
    return;
    }
else    
    Filt=Filt+Tec;
var input = Normalice(Filt);
var OptList = event.target.options;
for (var i = 0; i < OptList.length; i++) 
    {
    if (Normalice(OptList[i].text).indexOf(input) == -1) 
        {
        OptList[i].selected = false;
        OptList[i].style.display = "none";
        }
    else    
        {
        if (!first)    
            {
            OptList[i].selected = true;
            first=true;
            }
        else    
            OptList[i].selected = false;
        OptList[i].style.display = "block";
        }
    }
}
//-----------------------------------------------------
function Clean(event)
{
Filt="";
var OptList = event.target.options;
for (var i = 0; i < OptList.length; i++) 
    {
    OptList[i].style.display = "block";
    }  
}
//-----------------------------------------------------
function Normalice(Text)
{
return(UnifyChars(Text).toLowerCase());    
}
//-----------------------------------------------------


