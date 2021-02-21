package com.klim.habrareader.app.windows.postsList.entities

import com.klim.habrareader.app.windows.postsList.enums.Commands

data class UpdateListCommand(val command: Commands, val position: Int) {

}
