package model

type Team struct {
	Id	string `json:"id"`
	Name string `json:"name"`
	Domain string `json:"domain"`
	Email string `json:"email"`
	CompanyName string `json:"company_name"`
}

func (team *Team) PreSave() {
	if team.Id == "" {
		team.Id = NewId()
	}
}

func (team *Team) IsValid() error {
	return nil
}
