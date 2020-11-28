package br.com.hioktec.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// classe não é mais utilizada substituida pela classe AtualizarStatusRequest
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaStatusDTO {
	
	private String status;
}
