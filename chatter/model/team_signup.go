package model

import (
	"encoding/json"
	"fmt"
	"io"
)

type TeamSignup struct {
	Team Team `json:"team"`
	User User `json:"user"`
	Data string	`json:"data"`
	Hash string `json:"hash"`
}

func TeamSignupFromJson(data io.Reader) *TeamSignup {
	decoder := json.NewDecoder(data)
	var signup TeamSignup
	err := decoder.Decode(&signup)
	if err == nil {
		return &signup
	} else {
		fmt.Println(err)
		return nil
	}
}
