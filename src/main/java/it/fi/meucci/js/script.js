let inputTitle = document.getElementById("title"),
    inputArtist = document.getElementById("artist"),
    inputLength = document.getElementById("length"),
    inputGenre = document.getElementById("genre"),

    tagError = document.getElementById("error"),
    tagTitleEr = document.getElementById("titleEr"),
    tagArtistEr = document.getElementById("artistEr"),
    tagLengthEr = document.getElementById("lengthEr"),
    tagGenreEr = document.getElementById("genreEr"),

    btnAdd = document.getElementById("add"),
    btnRemove = document.getElementById("remove"),
    btnModify = document.getElementById("modify"),
    btnSave = document.getElementById("save"),

    tagtitlesCell = document.getElementById("titles"),
    tagArtistsCell = document.getElementById("artists"),
    tagLengthsCell = document.getElementById("lengths"),
    tagGenresCell = document.getElementById("genres"),

    tagSongsNumber = document.getElementById("songsNumber"),
    tagTotalLength = document.getElementById("totalLength"),
    index = -1

    tempModifyBtn = btnModify, tempRemoveBtn = btnRemove
    playlist = []

function start(){
    if(JSON.parse(localStorage.getItem('playlist')) == null)
        return
    playlist = JSON.parse(localStorage.getItem('playlist'))
    updateInfo()
}

function addSong(){
    if(checkErrorAdd())
        return

    let tempSong = { titolo: "", artista: "", durata: "", genere: "" }
    
    tempSong.titolo = inputTitle.value
    tempSong.artista = inputArtist.value
    tempSong.durata = inputLength.value
    tempSong.genere = inputGenre.value

    playlist.push(tempSong)

    clearInputs()
    updateInfo()

}



function removeSong() {
    clearError()
    
        
    if(btnRemove.innerHTML == "Rimuovi"){
        if(checkError(inputTitle, tagTitleEr)){
            error.innerHTML = '* è obbligatorio compilare il campo "Titolo" per rimuovere un brano'
            return
        }else if (searchSong() == null){
            error.innerHTML = "non è stata trovato nessun brano con il titolo inserito"
            return
        }
        playlist.splice(searchSong(),1,) 
        
    }else{
        changeButtons()
        btnModify.innerHTML = "Modifica"
        btnRemove.innerHTML = "Rimuovi"
        
    }
    clearInputs()
    updateInfo()
}

function searchSong(){

    for (let i = 0; i < playlist.length; i++) 
        if (playlist[i].titolo == inputTitle.value) 
            return i
        
    return null
}


function modifyPlaylist(){
    clearError()
    if(checkError(inputTitle, tagTitleEr)){
        error.innerHTML = '* è obbligatorio compilare il campo "Titolo" per rimuovere un brano'
        return
    }else if (searchSong() == null && index == -1){
        error.innerHTML = "non è stata trovato nessun brano con il titolo inserito"
        return
    }

    if(btnModify.innerHTML == "Modifica"){
        
        index = searchSong()
        inputArtist.value = playlist[index].artista
        inputLength.value = playlist[index].durata
        inputGenre.value = playlist[index].genere
        changeButtons() 
        
        btnModify.innerHTML = "Salva Modifica"
        btnRemove.innerHTML = "Annulla Modifica"
    }else{
        let tempSong = { titolo: "", artista: "", durata: "", genere: "" }

        tempSong.titolo = inputTitle.value
        tempSong.artista = inputArtist.value
        tempSong.durata = inputLength.value
        tempSong.genere = inputGenre.value

        playlist.splice(index, 1,tempSong)

        changeButtons() 
        btnModify.innerHTML = "Modifica"
        btnRemove.innerHTML = "Rimuovi"
        index = -1
        clearInputs()
    }
    
    updateInfo()
}

function changeButtons(){
    
    btnAdd.disabled = !btnAdd.disabled
    btnSave.disabled = !btnSave.disabled
    
    /*DOMANDA
    let parent = btnRemove.parentNode,
        newRemove = document.createElement("button"), newModify = document.createElement("button")
    
    newRemove.id = "remove"
    newRemove.setAttribute("class","btn btn-danger rounded")
    newRemove.setAttribute("onclick","cancelModify()")
    newRemove.innerHTML = "Annulla Modifica"
    parent.replaceChild(newRemove, btnRemove)
    
    newModify.id = "modify"
    newModify.setAttribute("class","btn btn-warning rounded")
    newModify.setAttribute("onclick","saveModify()")
    newModify.innerHTML = "Salva modifica"
    parent.replaceChild(newModify, btnModify)
    */
}



function savePlaylist(){
    playlist = localStorage.setItem('playlist', JSON.stringify(playlist))
}


function updateInfo() {
    tagtitlesCell.innerHTML = ""
    tagArtistsCell.innerHTML = ""
    tagLengthsCell.innerHTML = ""
    tagGenresCell.innerHTML = ""
    let somma = 0, conta = 0

    for(let i = 0; i < playlist.length; i++){
        somma += parseInt(playlist[i].durata) 
        conta++
        tagtitlesCell.innerHTML += playlist[i].titolo + "<br>"
        tagArtistsCell.innerHTML += playlist[i].artista + "<br>"
        tagLengthsCell.innerHTML += secondsToFullTime(playlist[i].durata) + "<br>"
        tagGenresCell.innerHTML += playlist[i].genere + "<br>"
    }
    tagSongsNumber.innerHTML = conta
    tagTotalLength.innerHTML = secondsToFullTime(somma)
   
}

function secondsToFullTime(length) {
    
    let hours, minutes, seconds

    hours = parseInt(length / 3600)
    minutes = parseInt((length - hours * 3600) / 60) 
    seconds = parseInt(length - hours * 3600 - minutes * 60)
    
    if (hours < 10) {
      hours = "0" + hours;
    }
    if (minutes < 10) {
      minutes = "0" + minutes;
    }
    if (seconds < 10) {
      seconds = "0" + seconds;
    }

    return hours+":"+minutes+":"+seconds
}

function checkError(input, tagEr){
    if(isEmpty(input)){
        tagEr.innerHTML = "*"
        return true
    } 
    return false
}

function checkErrorAdd(){
    clearError()
    if(isEmpty(inputTitle) || isEmpty(inputArtist) || isEmpty(inputLength) || isEmpty(inputGenre)){
        checkError(inputTitle, tagTitleEr)
        checkError(inputArtist, tagArtistEr)
        checkError(inputLength, tagLengthEr)
        checkError(inputGenre, tagGenreEr)
        tagError.innerHTML = " * è obbligatorio compilare i campi contrassegnati per inserire un brano"
        return true
    }else
        return false
}

function isEmpty(a){
    if(a.value.trim() == "")
        return true;
    return false
}

function clearError() {
    tagTitleEr.innerHTML = ""
    tagArtistEr.innerHTML = ""
    tagLengthEr.innerHTML = ""
    tagGenreEr.innerHTML = ""
    tagError.innerHTML = ""
}

function clearInputs(){
    inputTitle.value = ""
    inputArtist.value = ""
    inputLength.value = ""
    inputGenre.value = ""
}