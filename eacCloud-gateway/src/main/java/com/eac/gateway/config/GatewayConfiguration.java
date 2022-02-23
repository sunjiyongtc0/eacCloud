package com.eac.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sentinel高可用流控防护组件配置
 *
 */
@Configuration
public class GatewayConfiguration {

	private final List<ViewResolver> viewResolvers;
	private final ServerCodecConfigurer serverCodecConfigurer;

	public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		// Register the block exception handler for Spring Cloud Gateway.
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@Bean
	@Order(-1)
	public GlobalFilter sentinelGatewayFilter() {
		return new SentinelGatewayFilter();
	}

	@PostConstruct
	public void doInit() {
		initCustomizedApis();
		initGatewayRules();
	}

	private void initCustomizedApis() {
		Set<ApiDefinition> definitions = new HashSet<>();
		ApiDefinition api1 = new ApiDefinition("filter_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			{
				add(new ApiPathPredicateItem().setPattern("/api/uaa/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/account/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/admin/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/bigdata/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/chart/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/file/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/notification/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/statistics/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/activiti/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/generator/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		ApiDefinition api2 = new ApiDefinition("another_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			{
				add(new ApiPathPredicateItem().setPattern("/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		definitions.add(api1);
		definitions.add(api2);
		GatewayApiDefinitionManager.loadApiDefinitions(definitions);
	}

	private void initGatewayRules() {
		Set<GatewayFlowRule> rules = new HashSet<>();

		rules.add(new GatewayFlowRule("eac-auth").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("eac-auth").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("eac-base-account").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("eac-base-account").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("eac-base-admin").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("eac-base-admin").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("eac-pro-bigdata").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("eac-pro-bigdata").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-chart-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-chart-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-file-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-file-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-notification-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-notification-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-statistics-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-statistics-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-activiti-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-activiti-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-generator-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-generator-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		GatewayRuleManager.loadRules(rules);
	}

	/**
	 * 配置Prometheus
	 *
	 * @param applicationName 应用名称
	 * @return
	 */
	@Bean
	public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}

}
