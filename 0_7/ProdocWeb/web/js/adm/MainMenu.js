/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU GPL v3 http://www.gnu.org/licenses/gpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2011
 * 
 */

var activeSub=0;
var SubNum=0;

var timerID = null;
var timerOn = false;
var TimeOut = 600;
var BrowType = null;
var newbrowser = true;
var check = false;

//-------------------------------------------------------
function reDo()
{
window.location.reload()
}
window.onresize = reDo;
//------------------------------------------------------------------------
function init()
{
//  alert ("Running Init");
if (document.layers) 
    {
    //  alert ("Running Netscape 4");
    layerRef="document.layers";
    styleSwitch="";
    visibleVar="show";
    screenSize = window.innerWidth;
    BrowType ="ns4";
    }
else if (document.all)
    {
    //  alert ("Running IE");
    layerRef="document.all";
    styleSwitch=".style";
    visibleVar="visible";
    screenSize = document.body.clientWidth + 18;
    BrowType ="ie";
    }
else if (document.getElementById)
    {
    //  alert ("Running Netscape 6");
    layerRef="document.getElementByID";
    styleSwitch=".style";
    visibleVar="visible";
    BrowType="moz";
    }
else
    {
    //alert("Older than 4.0 browser.");
    BrowType="none";
    newbrowser = false;
    }
window.status='status bar text to go here';
check = true;
}
//------------------------------------------------------------------------
// Turns the layers on and off
function showLayer(layerName)
{
if(check)
    {
    if (BrowType =="none")
        {
        return;
        }
    else if (BrowType == "moz")
        {
        document.getElementById(layerName).style.visibility="visible";
        document.getElementById(layerName).style.top=document.getElementById("MenuLine").clientHeight+document.getElementById("MenuLine").offsetTop+15
        }
    else
        {
        eval(layerRef+'["'+layerName+'"]'+styleSwitch+'.visibility="visible"');
        }
    }
else 
    return;
}
//------------------------------------------------------------------------
function hideLayer(layerName)
{
if(check)
    {
    if (BrowType =="none")
        {
        return;
        }
    else if (BrowType == "moz")
        {
        document.getElementById(layerName).style.visibility="hidden";
        }
    else
        {
        eval(layerRef+'["'+layerName+'"]'+styleSwitch+'.visibility="hidden"');
        }
    }
else
    return;
}
//------------------------------------------------------------------------
function hideAll()
{
try {
hideLayer('MCol0');
} catch(err)
       { }
try {
hideLayer('MCol1');
} catch(err)
       { }
try {
hideLayer('MCol2');
} catch(err)
       { }
try {
hideLayer('MCol3');
} catch(err)
       { }}
//------------------------------------------------------------------------
function startTime()
{
if (timerOn == false) 
    {
    timerID=setTimeout( "hideAll()" , TimeOut);
    timerOn = true;
    }
}
//------------------------------------------------------------------------
function stopTime()
{
if (timerOn) 
    {
    clearTimeout(timerID);
    timerID = null;
    timerOn = false;
    }
}
//------------------------------------------------------------------------
function onLoad()
{
init();
}
//------------------------------------------------------------------------
