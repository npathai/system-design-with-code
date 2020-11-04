package model

import (
	"encoding/json"
	"io"
)

type Team struct {
	Id	string `json:"id"`
	Name string `json:"name"`
	Domain string `json:"domain"`
	Email string `json:"email"`
	CompanyName string `json:"company_name"`
}

func TeamFromJson(data io.Reader) *Team {
	var o *Team
	json.NewDecoder(data).Decode(&o)
	return o
}

func (o *Team) ToJson() string {
	b, _ := json.Marshal(o)
	return string(b)
}

func (team *Team) PreSave() {
	if team.Id == "" {
		team.Id = NewId()
	}
}

func (team *Team) IsValid() *AppError {
	if len(team.Id) != 26 {
		return NewAppError("Invalid Id")
	}

	if len(team.Email) > 128 {
		return NewAppError("Invalid email")
	}

	if !IsValidEmail(team.Email) {
		return NewAppError("Invalid email")
	}

	if len(team.Name) > 64 {
		return NewAppError("Invalid name")
	}

	if len(team.Domain) > 64 {
		return NewAppError("Invalid domain")
	}

	if !IsValidDomain(team.Domain) {
		return NewAppError("Invalid domain")
	}

	if len(team.CompanyName) > 64 {
		return NewAppError("Invalid company name")
	}

	return nil
}

