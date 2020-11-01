package model

import (
	"encoding/json"
	"io"
)

type User struct {
	Id	string	`json:"id"`
	Username           string    `json:"username"`
	Password           string    `json:"password"`
	AuthData           string    `json:"auth_data"`
	Email              string    `json:"email"`
	FirstName           string    `json:"first_name"`
	LastName			string `json:"last_name"`
	Nickname			string `json:"nickname"`
}

func UserFromJson(data io.Reader) *User {
	var user *User
	json.NewDecoder(data).Decode(&user)
	return user
}

func (user *User) ToJson() string {
	bytes, _ := json.Marshal(user)
	return string(bytes)
}