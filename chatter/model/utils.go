package model

import (
	"bytes"
	"encoding/base32"
	"net/mail"
	"regexp"
	"strings"
)

var encoding = base32.NewEncoding("ybndrfg8ejkmcpqxot1uwisza345h769")

func NewId() string {
 	var b bytes.Buffer
	encoder :=base32.NewEncoder(encoding, &b)
	encoder.Write(uuid.NewRandom())
	encoder.Close()
	b.Truncate(26)
	return b.String()
}

type AppError struct {
	StatusCode int `json:"status_code"`
	Message string `json:"message"`
}

func (err *AppError) Error() string {
	return err.Message
}

func NewAppError(message string) *AppError {
	return &AppError{
		StatusCode: 500,
		Message: message,
	}
}

func IsValidEmail(email string) bool {
	if !IsLower(email) {
		return false
	}

	if _, err := mail.ParseAddress(email); err != nil {
		return false
	}

	return true
}

func IsLower(email string) bool {
	return strings.ToLower(email) == email
}

func IsValidDomain(domain string) bool {
	if !IsValidAlphaNum(domain) {
		return false
	}
	if len(domain) <= 3 {
		return false
	}
	return true
}

var validAlphaNum = regexp.MustCompile(`^[a-z0-9]+([a-z\-0-9]+|(__)?)[a-z0-9]+$`)
func IsValidAlphaNum(str string) bool {
	return validAlphaNum.MatchString(str)
}